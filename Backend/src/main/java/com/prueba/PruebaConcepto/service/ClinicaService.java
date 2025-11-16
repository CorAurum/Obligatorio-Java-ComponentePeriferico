package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClinicaService {

    private final ClinicaRepository clinicaRepository;

    public ClinicaService(ClinicaRepository clinicaRepository) {
        this.clinicaRepository = clinicaRepository;
    }

    public Clinica crearClinica(Clinica clinica) {
        if (clinicaRepository.existsByNombre(clinica.getNombre())) {
            throw new IllegalArgumentException("Ya existe una clínica con ese nombre");
        }

        // Generate UUID for the clinic ID (this will be used as tenantId)
        clinica.setId(UUID.randomUUID().toString());
        clinica.setFechaAlta(LocalDateTime.now());

        return clinicaRepository.save(clinica);
    }

    public List<Clinica> listarClinicas() {
        return clinicaRepository.findAll();
    }

    public Optional<Clinica> obtenerPorDominio(String dominio) {
        return clinicaRepository.findByDominioSubdominio(dominio);
    }
}
