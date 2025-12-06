package com.prueba.PruebaConcepto.config;

import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.entity.Especialidad;
import com.prueba.PruebaConcepto.entity.EstadoProblema;
import com.prueba.PruebaConcepto.entity.GradoCerteza;
import com.prueba.PruebaConcepto.entity.MotivoConsulta;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.entity.Role;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.repository.AdministradorRepository;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import com.prueba.PruebaConcepto.repository.EspecialidadRepository;
import com.prueba.PruebaConcepto.repository.EstadoProblemaRepository;
import com.prueba.PruebaConcepto.repository.GradoCertezaRepository;
import com.prueba.PruebaConcepto.repository.MotivoConsultaRepository;
import com.prueba.PruebaConcepto.repository.ProfesionalDeSaludRepository;
import com.prueba.PruebaConcepto.repository.UsuarioDeSaludRepository;
import com.prueba.PruebaConcepto.repository.DocumentoClinicoRepository;
import com.prueba.PruebaConcepto.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Seeds test data for authentication testing
 * Runs automatically on application startup unless "prod" profile is active
 * 
 * Test Credentials:
 * Admin: cedula = "12345678", password = "Password123"
 * Professional: cedula = "87654321", password = "Password123"
 * 
 * To disable: Set spring.profiles.active=prod in application.properties
 */
@Component
@Profile("!prod") // Run by default, skip only in production
@RequiredArgsConstructor
public class TestDataSeeder implements CommandLineRunner {

    private final ClinicaRepository clinicaRepository;
    private final AdministradorRepository administradorRepository;
    private final ProfesionalDeSaludRepository profesionalDeSaludRepository;
    private final EspecialidadRepository especialidadRepository;
    private final MotivoConsultaRepository motivoConsultaRepository;
    private final GradoCertezaRepository gradoCertezaRepository;
    private final EstadoProblemaRepository estadoProblemaRepository;
    private final UsuarioDeSaludRepository usuarioDeSaludRepository;
    private final DocumentoClinicoRepository documentoClinicoRepository;
    private final AuthService authService;

    @Override
    public void run(String... args) {
        System.out.println("Seeding/updating test data for authentication...");

        // 1. Create test clinic
        Clinica testClinica = createTestClinica();

        // 2. Create test especialidad
        Especialidad testEspecialidad = createTestEspecialidad();

        // 2b. Seed clinical catalogs used by dropdowns
        seedCatalogosClinicos();

        // 3. Create or update test administrator (always update password to ensure it's
        // correct)
        createOrUpdateTestAdministrator(testClinica);

        // 4. Create or update test professional (always update password to ensure it's
        // correct)
        createOrUpdateTestProfessional(testClinica, testEspecialidad);

        // 5. Create test patient and example document
        UsuarioDeSalud paciente = createOrUpdateTestPatient(testClinica);
        createSampleDocumentoClinico(testClinica, testEspecialidad, paciente);

        System.out.println("Test data seeded/updated successfully!");
        System.out.println("========================================");
        System.out.println("TEST USERS (updated with correct passwords):");
        System.out.println("========================================");
        System.out.println("1. ADMINISTRATOR:");
        System.out.println("   Cedula: 12345678");
        System.out.println("   Password: Password123");
        System.out.println("   Role: ADMINISTRADOR");
        System.out.println("   Email: admin@testclinic.com");
        System.out.println("");
        System.out.println("2. PROFESSIONAL:");
        System.out.println("   Cedula: 87654321");
        System.out.println("   Password: Password123");
        System.out.println("   Role: PROFESIONAL");
        System.out.println("   Email: maria.profesional@testclinic.com");
        System.out.println("========================================");
    }

    private Clinica createTestClinica() {
        // Look up by the numeric-looking ID
        Clinica clinica = clinicaRepository.findById("1")
                .orElse(new Clinica());

        // Use numeric-looking IDs to align with incremental expectations
        clinica.setId("1");
        clinica.setNombre("suat");
        clinica.setDireccion("Av. Test 123");
        clinica.setTelefono("+59899123456");
        // Use the same value for dominioSubdominio as the test clinic ID so lookups
        // via /api/clinicas/{dominioSubdominio} and ?dominioSubdominio=... resolve
        // correctly.
        clinica.setDominioSubdominio("suat");
        clinica.setFechaAlta(LocalDateTime.now());
        clinica.setFechaBaja(null);
        clinica.setTipoInstitucion("Privada");

        return clinicaRepository.save(clinica);
    }

    private Especialidad createTestEspecialidad() {
        Especialidad especialidad = especialidadRepository.findById("1")
                .orElse(new Especialidad());

        especialidad.setId("1");
        especialidad.setNombre("Medicina General");
        especialidad.setDescripcion("Especialidad de medicina general para testing");

        return especialidadRepository.save(especialidad);
    }

    private void createOrUpdateTestAdministrator(Clinica clinica) {
        Administrador admin = administradorRepository.findByCedula("12345678")
                .orElse(new Administrador());

        admin.setNombre("Juan");
        admin.setApellido("Administrador");
        admin.setCedula("12345678");
        admin.setEmail("admin@testclinic.com");
        admin.setUsuario("jadmin");
        admin.setCreadorPor("system");
        admin.setActivo(true);
        admin.setRole(Role.ADMINISTRADOR);
        admin.setClinica(clinica);

        // Always update password to ensure it's correct (Password123)
        if (admin.getId() != null) {
            // User exists, update password
            authService.updateAdminPassword(admin, "Password123");
        } else {
            // New user, create with hashed password
            authService.createAdminWithHashedPassword(admin, "Password123");
        }
    }

    private void createOrUpdateTestProfessional(Clinica clinica, Especialidad especialidad) {
        ProfesionalDeSalud profesional = profesionalDeSaludRepository.findByCedulaIdentidad("87654321")
                .orElse(new ProfesionalDeSalud());

        profesional.setIdProfesional("1");
        profesional.setCedulaIdentidad("87654321");
        profesional.setNombre("Dra. María");
        profesional.setApellido("Profesional");
        profesional.setEmail("maria.profesional@testclinic.com");
        profesional.setTelefono("+59898765432");
        profesional.setActivo(true);
        profesional.setRole(Role.PROFESIONAL);
        profesional.setClinica(clinica);
        profesional.setEspecialidades(Arrays.asList(especialidad));

        // Always update password to ensure it's correct (Password123)
        if (profesional.getIdProfesional() != null
                && profesionalDeSaludRepository.existsById(profesional.getIdProfesional())) {
            // User exists, update password
            authService.updateProfesionalPassword(profesional, "Password123");
        } else {
            // New user, create with hashed password
            authService.createProfesionalWithHashedPassword(profesional, "Password123");
        }
    }

    /**
     * Seed static catalogs required by the frontend dropdowns.
     * Safe to run repeatedly; it only inserts when absent.
     */
    private void seedCatalogosClinicos() {
        if (motivoConsultaRepository.count() == 0) {
            motivoConsultaRepository.saveAll(Arrays.asList(
                    buildMotivo(null, "Consulta general"),
                    buildMotivo(null, "Chequeo de seguimiento"),
                    buildMotivo(null, "Resultados de estudios")));
        }

        if (gradoCertezaRepository.count() == 0) {
            gradoCertezaRepository.saveAll(Arrays.asList(
                    buildGrado(null, "presuntivo"),
                    buildGrado(null, "confirmado")));
        }

        if (estadoProblemaRepository.count() == 0) {
            estadoProblemaRepository.saveAll(Arrays.asList(
                    buildEstado(null, "problema resuelto"),
                    buildEstado(null, "problema no resuelto")));
        }
    }

    private MotivoConsulta buildMotivo(Long id, String motivo) {
        MotivoConsulta m = new MotivoConsulta();
        m.setId(id);
        m.setMotivo(motivo);
        return m;
    }

    private GradoCerteza buildGrado(Long id, String nombre) {
        GradoCerteza g = new GradoCerteza();
        g.setId(id);
        g.setNombre(nombre);
        return g;
    }

    private EstadoProblema buildEstado(Long id, String nombre) {
        EstadoProblema e = new EstadoProblema();
        e.setId(id);
        e.setNombre(nombre);
        return e;
    }

    private UsuarioDeSalud createOrUpdateTestPatient(Clinica clinica) {
        UsuarioDeSalud usuario = usuarioDeSaludRepository.findByEmail("paciente@testclinic.com")
                .orElse(new UsuarioDeSalud());

        usuario.setNombre("Paciente");
        usuario.setApellido("Prueba");
        usuario.setEmail("paciente@testclinic.com");
        usuario.setTelefono("+59891234567");
        usuario.setDireccion("Av. Paciente 123");
        usuario.setFechaNacimiento(java.time.LocalDate.of(1990, 1, 1));
        usuario.setSexo("F");
        usuario.setClinica(clinica);
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        return usuarioDeSaludRepository.save(usuario);
    }

    private void createSampleDocumentoClinico(Clinica clinica, Especialidad especialidad, UsuarioDeSalud usuario) {
        // Ensure we have the professional
        ProfesionalDeSalud profesional = profesionalDeSaludRepository.findByCedulaIdentidad("87654321")
                .orElse(null);
        if (profesional == null) {
            return; // Should not happen, but avoid NPE
        }

        // Avoid duplicating sample document
        if (documentoClinicoRepository.existsById("1")) {
            return;
        }

        List<MotivoConsulta> motivos = motivoConsultaRepository.findAll();
        MotivoConsulta motivo = motivos.isEmpty() ? null : motivos.get(0);

        com.prueba.PruebaConcepto.entity.DocumentoClinico doc = new com.prueba.PruebaConcepto.entity.DocumentoClinico();
        doc.setId("1");
        doc.setArea("Medicina General");
        doc.setAreaProximoControl("Seguimiento");
        doc.setTitulo("Control inicial");
        doc.setDescripcion("Nota clínica de prueba para validar la UI del profesional.");
        doc.setTipoDocumento("NOTA");
        doc.setUrlAlojamiento("http://localhost:8081/mock/doc-test-001");
        doc.setFechaProximaConsultaRecomendada(LocalDateTime.now().plusDays(30));
        doc.setFechaProximaConsultaConfirmada(null);
        doc.setUsuario(usuario);
        doc.setProfesional(profesional);
        doc.setClinica(clinica);
        doc.setMotivosConsulta(motivo != null ? Collections.singletonList(motivo) : Collections.emptyList());
        doc.setDiagnosticos(Collections.emptyList());

        documentoClinicoRepository.save(doc);
    }
}
