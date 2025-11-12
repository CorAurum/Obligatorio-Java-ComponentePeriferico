package com.prueba.PruebaConcepto.service;



import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        return clinicaRepository.save(clinica);
    }

    public List<Clinica> listarClinicas() {
        return clinicaRepository.findAll();
    }

    public Optional<Clinica> obtenerPorDominio(String dominio) {
        return clinicaRepository.findByDominioSubdominio(dominio);
    }
}
