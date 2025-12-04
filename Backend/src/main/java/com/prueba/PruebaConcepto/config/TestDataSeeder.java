package com.prueba.PruebaConcepto.config;

import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.entity.Especialidad;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.entity.Role;
import com.prueba.PruebaConcepto.repository.AdministradorRepository;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import com.prueba.PruebaConcepto.repository.EspecialidadRepository;
import com.prueba.PruebaConcepto.repository.ProfesionalDeSaludRepository;
import com.prueba.PruebaConcepto.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

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
    private final AuthService authService;

    @Override
    public void run(String... args) {
        System.out.println("Seeding/updating test data for authentication...");

        // 1. Create test clinic
        Clinica testClinica = createTestClinica();

        // 2. Create test especialidad
        Especialidad testEspecialidad = createTestEspecialidad();

        // 3. Create or update test administrator (always update password to ensure it's
        // correct)
        createOrUpdateTestAdministrator(testClinica);

        // 4. Create or update test professional (always update password to ensure it's
        // correct)
        createOrUpdateTestProfessional(testClinica, testEspecialidad);

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
        Clinica clinica = clinicaRepository.findById("test-clinic-auth-001")
                .orElse(new Clinica());

        clinica.setId("test-clinic-auth-001");
        clinica.setNombre("suat");
        clinica.setDireccion("Av. Test 123");
        clinica.setTelefono("+59899123456");
        clinica.setDominioSubdominio("suat");
        clinica.setFechaAlta(LocalDateTime.now());
        clinica.setFechaBaja(null);
        clinica.setTipoInstitucion("Privada");

        return clinicaRepository.save(clinica);
    }

    private Especialidad createTestEspecialidad() {
        Especialidad especialidad = especialidadRepository.findById("esp-test-001")
                .orElse(new Especialidad());

        especialidad.setId("esp-test-001");
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

        profesional.setIdProfesional("prof-test-001");
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
}
