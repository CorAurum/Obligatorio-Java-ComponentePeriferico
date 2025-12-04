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


    public static class AdministradorDTO {
        public Long id;
        public String nombre;
        public String apellido;
        public String cedula;
        public String email;
        public String usuario;
        public Boolean activo;
        public String clinica;  // <-- here we send the clinic name
        public String creadorPor;

        public AdministradorDTO(Administrador a) {
            this.id = a.getId();
            this.nombre = a.getNombre();
            this.apellido = a.getApellido();
            this.cedula = a.getCedula();
            this.email = a.getEmail();
            this.usuario = a.getUsuario();
            this.activo = a.isActivo();
            this.clinica = (a.getClinica() != null ? a.getClinica().getNombre() : null);
            this.creadorPor = a.getCreadorPor();
        }
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


    public List<AdministradorDTO> listarTodos() {
        return administradorRepository.findAll()
                .stream()
                .map(AdministradorDTO::new)
                .toList();
    }

}



