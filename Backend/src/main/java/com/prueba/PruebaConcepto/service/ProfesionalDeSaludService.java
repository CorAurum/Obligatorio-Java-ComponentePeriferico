package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.ProfesionalCentralDTO;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.entity.Especialidad;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.repository.*;
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

    public ProfesionalDeSaludService(ProfesionalDeSaludRepository profesionalRepository,
                                     ClinicaRepository clinicaRepository,
                                     EspecialidadRepository especialidadRepository,
                                     CentralSyncService centralSyncService) {
        this.profesionalRepository = profesionalRepository;
        this.clinicaRepository = clinicaRepository;
        this.especialidadRepository = especialidadRepository;
        this.centralSyncService = centralSyncService;
    }

    @Transactional
    public ProfesionalDeSalud crearProfesional(ProfesionalDeSalud profesional, List<String> especialidadesNombres,
                                               String tenantId) {
        if (profesionalRepository.existsByEmail(profesional.getEmail())) {
            throw new IllegalArgumentException("Ya existe un profesional con ese correo");
        }
        if (profesionalRepository.existsByCedulaIdentidad(profesional.getCedulaIdentidad())) {
            throw new IllegalArgumentException("Ya existe un profesional con esa cédula");
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

        ProfesionalDeSalud guardado = profesionalRepository.save(profesional);

        // --- preparar DTO para enviar al central ---
        ProfesionalCentralDTO dto = new ProfesionalCentralDTO();
        dto.setId(guardado.getIdProfesional()); // si el periférico genera el id, lo enviamos; si no, se puede omitir (null)
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
}
