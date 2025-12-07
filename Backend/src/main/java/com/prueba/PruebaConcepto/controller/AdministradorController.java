package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.AdministradorRequest;
import com.prueba.PruebaConcepto.entity.Administrador;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.service.AdministradorService;
import com.prueba.PruebaConcepto.service.AuthService;
import com.prueba.PruebaConcepto.service.ClinicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;
    private final AuthService authService;
    private final ClinicaService clinicaService;

    public AdministradorController(AdministradorService administradorService,
            AuthService authService,
            ClinicaService clinicaService) {
        this.administradorService = administradorService;
        this.authService = authService;
        this.clinicaService = clinicaService;
    }

    // Crear administrador en la clínica especificada
    @PostMapping
    public ResponseEntity<?> crearAdministrador(@RequestBody AdministradorRequest request) {
        // Validate password is provided
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("La contraseña es requerida");
        }

        String tenantId = clinicaService.resolverTenantIdPorDominio(request.getDominioSubdominio());

        // Fetch the clinic and set it on the administrador before saving
        Clinica clinica = clinicaService.obtenerPorId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + tenantId));

        Administrador administrador = request.getAdministrador();
        administrador.setClinica(clinica);

        // Create administrator with hashed password
        Administrador nuevo = authService.createAdminWithHashedPassword(
                administrador,
                request.getPassword());

        return ResponseEntity.ok(nuevo);
    }

    // Listar administradores solo de la clínica especificada
    @GetMapping
    public ResponseEntity<List<Administrador>> listarAdministradores(@RequestParam String dominioSubdominio) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        return ResponseEntity.ok(administradorService.listarPorClinica(tenantId));
    }


    @GetMapping("/todos")
    public ResponseEntity<List<AdministradorService.AdministradorDTO>> listarTodosLosAdministradores() {
        return ResponseEntity.ok(administradorService.listarTodos());
    }

}
