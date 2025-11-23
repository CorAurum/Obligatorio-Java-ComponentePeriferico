package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.UsuarioRequest;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.service.ClinicaService;
import com.prueba.PruebaConcepto.service.UsuarioDeSaludService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioDeSaludController {

    private final UsuarioDeSaludService usuarioService;
    private final ClinicaService clinicaService;

    public UsuarioDeSaludController(UsuarioDeSaludService usuarioService, ClinicaService clinicaService) {
        this.usuarioService = usuarioService;
        this.clinicaService = clinicaService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDeSalud> crearUsuario(@RequestBody UsuarioRequest request) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(request.getDominioSubdominio());
        UsuarioDeSalud creado = usuarioService.crearUsuarioDesdeRequest(request, tenantId);
        return ResponseEntity.status(201).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDeSalud>> listarUsuarios(@RequestParam String dominioSubdominio) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        return ResponseEntity.ok(usuarioService.listarPorClinica(tenantId));
    }
}
