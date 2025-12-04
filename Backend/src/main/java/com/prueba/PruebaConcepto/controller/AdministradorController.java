package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.AdministradorRequest;
import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.service.AdministradorService;
import com.prueba.PruebaConcepto.service.ClinicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;
    private final ClinicaService clinicaService;

    public AdministradorController(AdministradorService administradorService, ClinicaService clinicaService) {
        this.administradorService = administradorService;
        this.clinicaService = clinicaService;
    }

    // Crear administrador en la clínica especificada
    @PostMapping
    public ResponseEntity<Administrador> crearAdministrador(@RequestBody AdministradorRequest request) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(request.getDominioSubdominio());
        Administrador nuevo = administradorService.crearAdministrador(request.getAdministrador(),
                tenantId);
        return ResponseEntity.ok(nuevo);
    }

    // Listar administradores solo de la clínica especificada
    @GetMapping
    public ResponseEntity<List<Administrador>> listarAdministradores(@RequestParam String dominioSubdominio) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        return ResponseEntity.ok(administradorService.listarPorClinica(tenantId));
    }


    @GetMapping("/todos")
    public ResponseEntity<List<Administrador>> listarTodosLosAdministradores() {
        return ResponseEntity.ok(administradorService.listarTodos());
    }

}
