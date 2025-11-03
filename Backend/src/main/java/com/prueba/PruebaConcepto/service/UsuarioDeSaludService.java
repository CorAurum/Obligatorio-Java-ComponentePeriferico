package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.IdentificadorRequest;
import com.prueba.PruebaConcepto.Dto.UsuarioRequest;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.entity.IdentificadorUsuario;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import com.prueba.PruebaConcepto.repository.UsuarioDeSaludRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioDeSaludService {

    private final UsuarioDeSaludRepository usuarioRepository;
    private final ClinicaRepository clinicaRepository;

    public UsuarioDeSaludService(UsuarioDeSaludRepository usuarioRepository, ClinicaRepository clinicaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clinicaRepository = clinicaRepository;
    }

    @Transactional
    public UsuarioDeSalud crearUsuarioDesdeRequest(Long clinicaId, UsuarioRequest request) {
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + clinicaId));

        // Validaciones básicas (evitá duplicados por email o por identificador si corresponde)
        // ejemplo: if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) { throw... }

        // Mapear DTO -> entidad UsuarioDeSalud
        UsuarioDeSalud usuario = new UsuarioDeSalud();
        usuario.setNombre(request.getNombres());
        usuario.setApellido(request.getApellidos());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setSexo(request.getSexo());
        usuario.setDireccion(request.getDireccion());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setClinica(clinica);
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        // Mapear identificadores (si llegaron)
        if (request.getIdentificadores() != null && !request.getIdentificadores().isEmpty()) {
            List<IdentificadorUsuario> identificadores = new ArrayList<>();
            for (IdentificadorRequest idReq : request.getIdentificadores()) {
                IdentificadorUsuario ident = new IdentificadorUsuario();
                ident.setTipo(idReq.getTipo());
                ident.setValor(idReq.getValor());
                ident.setFechaAlta(LocalDateTime.now());

                // Asociar al usuario (importante: bidireccional si querés)
                ident.setUsuario(usuario);

                identificadores.add(ident);
            }
            usuario.setIdentificadores(identificadores);
        }

        // Guardar (por cascade, los IdentificadorUsuario se guardan automáticamente)
        return usuarioRepository.save(usuario);
    }

    public List<UsuarioDeSalud> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<UsuarioDeSalud> listarPorClinica(Long clinicaId) {
        return usuarioRepository.findByClinicaId(clinicaId);
    }
}
