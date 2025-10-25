package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.ProfesionalDeSaludDto;
import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.repository.ProfesionalDeSaludRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesionalDeSaludService {

    private final ProfesionalDeSaludRepository repo;

    public ProfesionalDeSaludService(ProfesionalDeSaludRepository repo) {
        this.repo = repo;
    }

    public ProfesionalDeSaludDto crearProfesional(ProfesionalDeSaludDto dto) {
        ProfesionalDeSalud p = new ProfesionalDeSalud(
                null,
                dto.cedulaIdentidad(),
                dto.nombre(),
                dto.apellido(),
                dto.numeroRegistro(),
                dto.email(),
                dto.telefono()
        );
        return toDto(repo.save(p));
    }

    public List<ProfesionalDeSaludDto> listarProfesionales() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private ProfesionalDeSaludDto toDto(ProfesionalDeSalud p) {
        return new ProfesionalDeSaludDto(
                p.getIdProfesional(),
                p.getCedulaIdentidad(),
                p.getNombre(),
                p.getApellido(),
                p.getNumeroRegistro(),
                p.getEmail(),
                p.getTelefono()
        );
    }
}
