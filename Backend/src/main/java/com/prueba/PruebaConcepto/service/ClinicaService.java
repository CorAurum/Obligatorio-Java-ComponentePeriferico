package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.repository.AdministradorRepository;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import com.prueba.PruebaConcepto.repository.ProfesionalDeSaludRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClinicaService {

    private final ClinicaRepository clinicaRepository;

    private final ProfesionalDeSaludRepository profesionalDeSaludRepository;

    private final AdministradorRepository administradorRepository;

    public ClinicaService(ClinicaRepository clinicaRepository,
            ProfesionalDeSaludRepository profesionalDeSaludRepository,
            AdministradorRepository administradorRepository) {
        this.clinicaRepository = clinicaRepository;
        this.profesionalDeSaludRepository = profesionalDeSaludRepository;
        this.administradorRepository = administradorRepository;
    }

    public Clinica crearClinica(Clinica clinica) {
        if (clinicaRepository.existsByNombre(clinica.getNombre())) {
            throw new IllegalArgumentException("Ya existe una clínica con ese nombre");
        }

        if (clinicaRepository.findByDominioSubdominio(clinica.getDominioSubdominio()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una clínica con ese dominio");
        }

        // Generate UUID for the clinic ID (this will be used as tenantId)
        clinica.setId(UUID.randomUUID().toString());
        clinica.setFechaAlta(LocalDateTime.now());

        return clinicaRepository.save(clinica);
    }

    public List<Clinica> listarClinicas() {
        return clinicaRepository.findAll();
    }

    public Optional<Clinica> obtenerPorId(String id) {
        return clinicaRepository.findById(id);
    }

    public Optional<Clinica> obtenerPorDominio(String dominio) {
        return clinicaRepository.findByDominioSubdominio(dominio);
    }

    public Optional<String> obtenerDominioPorCedula(String cedula) {

        // 1. Buscar coincidencia en Profesionales de Salud
        Optional<ProfesionalDeSalud> profesionalOpt = profesionalDeSaludRepository.findByCedulaIdentidad(cedula);

        if (profesionalOpt.isPresent()) {
            Clinica clinica = profesionalOpt.get().getClinica();
            if (clinica != null) {
                return Optional.of(clinica.getDominioSubdominio());
            }
        }

        // 2. Si no hay profesional, buscar en Administradores
        Optional<Administrador> adminOpt = administradorRepository.findByCedula(cedula); // o findByEmail si preferís

        if (adminOpt.isPresent()) {
            Clinica clinica = adminOpt.get().getClinica();
            if (clinica != null) {
                return Optional.of(clinica.getDominioSubdominio());
            }
        }

        // 3. No existe en ninguna tabla
        return Optional.empty();
    }

    public String resolverTenantIdPorDominio(String dominio) {
        return clinicaRepository.findByDominioSubdominio(dominio)
                .map(Clinica::getId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con dominio: " + dominio));
    }

    public com.prueba.PruebaConcepto.Dto.ClinicaDTO obtenerClinicaDTO(String dominio) {
        Clinica clinica = clinicaRepository.findByDominioSubdominio(dominio)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con dominio: " + dominio));

        com.prueba.PruebaConcepto.Dto.ClinicaDTO dto = new com.prueba.PruebaConcepto.Dto.ClinicaDTO();
        dto.setId(clinica.getId());
        dto.setNombre(clinica.getNombre());
        dto.setDominioSubdominio(clinica.getDominioSubdominio());
        dto.setDireccion(clinica.getDireccion());
        dto.setTelefono(clinica.getTelefono());
        dto.setTipoInstitucion(clinica.getTipoInstitucion());
        return dto;
    }

}
