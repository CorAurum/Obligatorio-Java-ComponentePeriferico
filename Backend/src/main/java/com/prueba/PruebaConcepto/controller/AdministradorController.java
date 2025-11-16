package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.AdministradorRequest;
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

    // Crear administrador en la clínica especificada
    @PostMapping
    public ResponseEntity<Administrador> crearAdministrador(@RequestBody AdministradorRequest request) {
        Administrador nuevo = administradorService.crearAdministrador(request.getAdministrador(),
                request.getTenantId());
        return ResponseEntity.ok(nuevo);
    }

    // Listar administradores solo de la clínica especificada
    @GetMapping
    public ResponseEntity<List<Administrador>> listarAdministradores(@RequestParam String tenantId) {
        return ResponseEntity.ok(administradorService.listarPorClinica(tenantId));
    }
}
