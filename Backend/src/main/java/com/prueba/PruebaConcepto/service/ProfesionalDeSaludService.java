package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.ProfesionalCentralDTO;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.entity.Especialidad;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfesionalDeSaludService {

    private final ProfesionalDeSaludRepository profesionalRepository;
    private final ClinicaRepository clinicaRepository;
    private final EspecialidadRepository especialidadRepository;
    private final CentralSyncService centralSyncService;
    private final AuthService authService;

    public ProfesionalDeSaludService(ProfesionalDeSaludRepository profesionalRepository,
            ClinicaRepository clinicaRepository,
            EspecialidadRepository especialidadRepository,
            CentralSyncService centralSyncService,
            AuthService authService) {
        this.profesionalRepository = profesionalRepository;
        this.clinicaRepository = clinicaRepository;
        this.especialidadRepository = especialidadRepository;
        this.centralSyncService = centralSyncService;
        this.authService = authService;
    }

    @Transactional
    public ProfesionalDeSalud crearProfesional(ProfesionalDeSalud profesional, List<String> especialidadesNombres,
            String tenantId) {
        if (profesionalRepository.existsByEmail(profesional.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un profesional con ese correo");
        }
        if (profesionalRepository.existsByCedulaIdentidad(profesional.getCedulaIdentidad())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un profesional con esa cédula");
        }

        Clinica clinica = clinicaRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + tenantId));

        List<Especialidad> especialidades = new ArrayList<>();
        for (String nombreEsp : especialidadesNombres) {
            Especialidad esp = especialidadRepository.findByNombre(nombreEsp)
                    .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada: " + nombreEsp));
            especialidades.add(esp);
        }

        profesional.setClinica(clinica);
        profesional.setEspecialidades(especialidades);
        profesional.setIdProfesional(UUID.randomUUID().toString());

        // Handle password: if password is provided, hash it; otherwise set a default
        // temporary password
        String rawPassword = profesional.getPassword();
        if (rawPassword == null || rawPassword.isEmpty()) {
            // Set a default temporary password that should be changed
            rawPassword = "TempPass123!";
        }

        // Hash and set the password using AuthService
        ProfesionalDeSalud guardado = authService.createProfesionalWithHashedPassword(profesional, rawPassword);

        // --- preparar DTO para enviar al central ---
        ProfesionalCentralDTO dto = new ProfesionalCentralDTO();
        dto.setId(guardado.getIdProfesional()); // si el periférico genera el id, lo enviamos; si no, se puede omitir
                                                // (null)
        dto.setNumeroRegistro("Lo borramos?");
        dto.setNombres(guardado.getNombre());
        dto.setApellidos(guardado.getApellido());
        dto.setEmail(guardado.getEmail());
        dto.setTelefono(guardado.getTelefono());
        // transformar especialidades a lista de ids
        List<String> especialidadesIds = guardado.getEspecialidades()
                .stream()
                .map(Especialidad::getId)
                .collect(Collectors.toList());
        dto.setEspecialidadesIds(especialidadesIds);

        // Enviar al central (no romper la operación local si falla el envío)
        try {
            centralSyncService.enviarProfesionalAlCentral(dto, tenantId);
        } catch (Exception ex) {
            // Loguear el error; no tiramos excepción para no romper la creación local.
            System.err.println("No se pudo sincronizar profesional con central: " + ex.getMessage());
        }

        return guardado;
    }

    public List<ProfesionalDeSalud> listarPorClinica(String tenantId) {
        return profesionalRepository.findByClinicaId(tenantId);
    }

    @Transactional
    public ProfesionalDeSalud actualizarProfesional(String id, ProfesionalDeSalud data,
            List<String> especialidadesNombres, String tenantId) {
        ProfesionalDeSalud profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));

        if (!profesional.getClinica().getId().equals(tenantId)) {
            throw new IllegalArgumentException("El profesional no pertenece a esta clínica");
        }

        if (data.getCedulaIdentidad() != null &&
                profesionalRepository.existsByCedulaIdentidadAndIdProfesionalNot(data.getCedulaIdentidad(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un profesional con esa cédula");
        }

        if (data.getEmail() != null &&
                profesionalRepository.existsByEmailAndIdProfesionalNot(data.getEmail(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un profesional con ese correo");
        }

        profesional.setCedulaIdentidad(data.getCedulaIdentidad());
        profesional.setNombre(data.getNombre());
        profesional.setApellido(data.getApellido());
        profesional.setEmail(data.getEmail());
        profesional.setTelefono(data.getTelefono());
        // No tocamos password ni rol aquí

        if (especialidadesNombres != null) {
            List<Especialidad> especialidades = new ArrayList<>();
            for (String nombreEsp : especialidadesNombres) {
                Especialidad esp = especialidadRepository.findByNombre(nombreEsp)
                        .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada: " + nombreEsp));
                especialidades.add(esp);
            }
            profesional.setEspecialidades(especialidades);
        }

        return profesionalRepository.save(profesional);
    }
}
