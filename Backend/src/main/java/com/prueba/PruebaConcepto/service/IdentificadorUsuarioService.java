package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.IdentificadorUsuario;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.repository.IdentificadorUsuarioRepository;
import com.prueba.PruebaConcepto.repository.UsuarioDeSaludRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IdentificadorUsuarioService {

    private final IdentificadorUsuarioRepository identificadorRepository;
    private final UsuarioDeSaludRepository usuarioRepository;

    public IdentificadorUsuarioService(IdentificadorUsuarioRepository identificadorRepository,
                                       UsuarioDeSaludRepository usuarioRepository) {
        this.identificadorRepository = identificadorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public IdentificadorUsuario crearIdentificador(String usuarioId, IdentificadorUsuario identificador) {
        UsuarioDeSalud usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        identificador.setUsuario(usuario);
        identificador.setFechaAlta(LocalDateTime.now());

        return identificadorRepository.save(identificador);
    }

    public List<IdentificadorUsuario> listarPorUsuario(Long usuarioId) {
        return identificadorRepository.findByUsuarioId(usuarioId);
    }

    public List<IdentificadorUsuario> listarTodos() {
        return identificadorRepository.findAll();
    }
}
