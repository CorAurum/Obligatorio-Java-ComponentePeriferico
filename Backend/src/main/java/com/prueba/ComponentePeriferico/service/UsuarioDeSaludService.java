package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.UsuarioDeSaludDto;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.repository.UsuarioDeSaludRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioDeSaludService {

    private final UsuarioDeSaludRepository repo;

    public UsuarioDeSaludService(UsuarioDeSaludRepository repo) {
        this.repo = repo;
    }

    public UsuarioDeSaludDto crearUsuario(UsuarioDeSaludDto dto) {
        UsuarioDeSalud u = new UsuarioDeSalud(
                dto.cedulaIdentidad(),
                dto.nombre(),
                dto.apellido(),
                dto.fechaNacimiento(),
                dto.email(),
                dto.telefono(),
                LocalDate.now()
        );
        return toDto(repo.save(u));
    }

    public List<UsuarioDeSaludDto> listarUsuarios() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private UsuarioDeSaludDto toDto(UsuarioDeSalud u) {
        return new UsuarioDeSaludDto(
                u.getCedulaIdentidad(),
                u.getNombre(),
                u.getApellido(),
                u.getFechaNacimiento(),
                u.getEmail(),
                u.getTelefono(),
                u.getFechaRegistro()
        );
    }
}
