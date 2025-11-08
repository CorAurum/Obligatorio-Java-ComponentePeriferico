package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.service.AdministradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    // Crear administrador en la clínica actual
    @PostMapping
    public ResponseEntity<Administrador> crearAdministrador(@RequestBody Administrador administrador) {
        Administrador nuevo = administradorService.crearAdministrador(administrador);
        return ResponseEntity.ok(nuevo);
    }

    // Listar administradores solo de la clínica actual
    @GetMapping
    public ResponseEntity<List<Administrador>> listarAdministradores() {
        return ResponseEntity.ok(administradorService.listarPorClinicaActual());
    }
}

