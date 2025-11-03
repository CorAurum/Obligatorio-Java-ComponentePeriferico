package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.UsuarioRequest;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.service.UsuarioDeSaludService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioDeSaludController {

    private final UsuarioDeSaludService usuarioService;

    public UsuarioDeSaludController(UsuarioDeSaludService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Crear usuario + identificadores en una sola request
    @PostMapping("/{clinicaId}")
    public ResponseEntity<UsuarioDeSalud> crearUsuario(
            @PathVariable Long clinicaId,
            @RequestBody UsuarioRequest request) {

        UsuarioDeSalud creado = usuarioService.crearUsuarioDesdeRequest(clinicaId, request);
        return ResponseEntity.status(201).body(creado);
    }

    // ... otros endpoints ...

    @GetMapping
    public ResponseEntity<List<UsuarioDeSalud>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/clinica/{clinicaId}")
    public ResponseEntity<List<UsuarioDeSalud>> listarPorClinica(@PathVariable Long clinicaId) {
        return ResponseEntity.ok(usuarioService.listarPorClinica(clinicaId));
    }
}
