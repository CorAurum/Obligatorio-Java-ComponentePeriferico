package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.entity.Especialidad;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import com.prueba.PruebaConcepto.repository.EspecialidadRepository;
import com.prueba.PruebaConcepto.repository.ProfesionalDeSaludRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesionalDeSaludService {

    private final ProfesionalDeSaludRepository profesionalRepository;
    private final ClinicaRepository clinicaRepository;
    private final EspecialidadRepository especialidadRepository;

    public ProfesionalDeSaludService(ProfesionalDeSaludRepository profesionalRepository,
                                     ClinicaRepository clinicaRepository,
                                     EspecialidadRepository especialidadRepository) {
        this.profesionalRepository = profesionalRepository;
        this.clinicaRepository = clinicaRepository;
        this.especialidadRepository = especialidadRepository;
    }

    public ProfesionalDeSalud crearProfesional(Long clinicaId, ProfesionalDeSalud profesional, List<String> especialidadesNombres) {
        if (profesionalRepository.existsByEmail(profesional.getEmail())) {
            throw new IllegalArgumentException("Ya existe un profesional con ese correo");
        }
        if (profesionalRepository.existsByCedulaIdentidad(profesional.getCedulaIdentidad())) {
            throw new IllegalArgumentException("Ya existe un profesional con esa cédula");
        }

        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + clinicaId));

        // Buscar las especialidades por nombre
        List<Especialidad> especialidades = new ArrayList<>();
        for (String nombreEsp : especialidadesNombres) {
            Especialidad esp = especialidadRepository.findByNombre(nombreEsp)
                    .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada: " + nombreEsp));
            especialidades.add(esp);
        }

        profesional.setClinica(clinica);
        profesional.setEspecialidades(especialidades);

        return profesionalRepository.save(profesional);
    }

    public List<ProfesionalDeSalud> listarPorClinica(Long clinicaId) {
        return profesionalRepository.findByClinicaId(clinicaId);
    }

    public List<ProfesionalDeSalud> listarTodos() {
        return profesionalRepository.findAll();
    }
}