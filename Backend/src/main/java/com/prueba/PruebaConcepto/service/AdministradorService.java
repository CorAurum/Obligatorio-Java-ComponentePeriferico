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

    public Administrador crearAdministrador(Administrador admin, String tenantId) {
        if (administradorRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Ya existe un administrador con ese correo");
        }

        Clinica clinica = clinicaRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + tenantId));

        admin.setClinica(clinica);
        admin.setActivo(true);
        return administradorRepository.save(admin);
    }

    public List<Administrador> listarPorClinica(String tenantId) {
        return administradorRepository.findByClinicaId(tenantId);
    }
}
