package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.UsuarioRequest;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.service.UsuarioDeSaludService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioDeSaludController {

    private final UsuarioDeSaludService usuarioService;

    public UsuarioDeSaludController(UsuarioDeSaludService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDeSalud> crearUsuario(@RequestBody UsuarioRequest request) {
        UsuarioDeSalud creado = usuarioService.crearUsuarioDesdeRequest(request);
        return ResponseEntity.status(201).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDeSalud>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarPorClinicaActual());
    }
}
