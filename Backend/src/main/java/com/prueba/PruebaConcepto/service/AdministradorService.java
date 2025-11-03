package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.repository.AdministradorRepository;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final ClinicaRepository clinicaRepository;

    public AdministradorService(AdministradorRepository administradorRepository, ClinicaRepository clinicaRepository) {
        this.administradorRepository = administradorRepository;
        this.clinicaRepository = clinicaRepository;
    }

    public Administrador crearAdministrador(Long clinicaId, Administrador admin) {
        if (administradorRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Ya existe un administrador con ese correo");
        }

        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + clinicaId));

        admin.setClinica(clinica);
        admin.setActivo(true);
        return administradorRepository.save(admin);
    }

    public List<Administrador> listarPorClinica(Long clinicaId) {
        return administradorRepository.findByClinicaId(clinicaId);
    }

    public List<Administrador> listarTodos() {
        return administradorRepository.findAll();
    }
}
