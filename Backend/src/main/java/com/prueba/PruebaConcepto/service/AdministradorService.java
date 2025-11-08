package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.repository.AdministradorRepository;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import com.prueba.PruebaConcepto.tenant.TenantContext;
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

    public Administrador crearAdministrador(Administrador admin) {
        if (administradorRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Ya existe un administrador con ese correo");
        }

        Long clinicaId = TenantContext.getClinicaId();
        if (clinicaId == null) {
            throw new IllegalStateException("No se encontró un tenant activo en el contexto");
        }

        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + clinicaId));

        admin.setClinica(clinica);
        admin.setActivo(true);
        return administradorRepository.save(admin);
    }

    public List<Administrador> listarPorClinicaActual() {
        Long clinicaId = TenantContext.getClinicaId();
        if (clinicaId == null) {
            throw new IllegalStateException("No se encontró un tenant activo en el contexto");
        }

        return administradorRepository.findByClinicaId(clinicaId);
    }
}


