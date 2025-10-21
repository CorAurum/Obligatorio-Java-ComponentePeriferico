package com.prueba.PruebaConcepto.service;

import java.util.List;
import java.util.stream.Collectors;
import com.prueba.PruebaConcepto.Dto.ClinicaDto;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import org.springframework.stereotype.Service;

@Service
public class ClinicaService {

    private final ClinicaRepository clinicaRepository;

    public ClinicaService(ClinicaRepository clinicaRepository) {
        this.clinicaRepository = clinicaRepository;
    }

    public ClinicaDto crearClinica(ClinicaDto dto) {
        Clinica p = new Clinica(
                null,
                dto.codigoClinica(),
                dto.nombre(),
                dto.direccion(),
                dto.telefono(),
                dto.tipoInstitucion()
        );
        Clinica saved = clinicaRepository.save(p);
        return new ClinicaDto(
                saved.getId(),
                saved.getCodigoClinica(),
                saved.getNombre(),
                saved.getDireccion(),
                saved.getTelefono(),
                saved.getTipoInstitucion()
        );
    }

    public List<ClinicaDto> listarClinicas() {
        return clinicaRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ClinicaDto modificarClinica(Long id, ClinicaDto dto) {
        Clinica p = clinicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinica no encontrada"));

        p.setCodigoClinica(dto.codigoClinica());
        p.setNombre(dto.nombre());
        p.setDireccion(dto.direccion());
        p.setTelefono(dto.telefono());
        p.setTipoInstitucion(dto.tipoInstitucion());

        Clinica updated = clinicaRepository.save(p);
        return toDto(updated);
    }

    public void eliminarClinica(Long id) {
        if (!clinicaRepository.existsById(id)) {
            throw new RuntimeException("Clinica no encontrada");
        }
        clinicaRepository.deleteById(id);
    }

    private ClinicaDto toDto(Clinica p) {
        return new ClinicaDto(
                p.getId(),
                p.getCodigoClinica(),
                p.getNombre(),
                p.getDireccion(),
                p.getTelefono(),
                p.getTipoInstitucion()
        );
    }
}
