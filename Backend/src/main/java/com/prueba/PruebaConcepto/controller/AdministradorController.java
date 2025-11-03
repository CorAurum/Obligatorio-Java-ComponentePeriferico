package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.service.AdministradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
//@CrossOrigin(origins = "*")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    // Crear administrador asociado a una clínica
    @PostMapping("/{clinicaId}")
    public ResponseEntity<Administrador> crearAdministrador(
            @PathVariable Long clinicaId,
            @RequestBody Administrador administrador) {

        Administrador nuevo = administradorService.crearAdministrador(clinicaId, administrador);
        return ResponseEntity.ok(nuevo);
    }

    // Listar todos los administradores
    @GetMapping
    public ResponseEntity<List<Administrador>> listarTodos() {
        return ResponseEntity.ok(administradorService.listarTodos());
    }

    // Listar administradores de una clínica específica
    @GetMapping("/clinica/{clinicaId}")
    public ResponseEntity<List<Administrador>> listarPorClinica(@PathVariable Long clinicaId) {
        return ResponseEntity.ok(administradorService.listarPorClinica(clinicaId));
    }
}
