package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.service.ClinicaService;
import com.prueba.PruebaConcepto.service.ProfesionalDeSaludService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalDeSaludController {

    private final ProfesionalDeSaludService profesionalService;
    private final ClinicaService clinicaService;

    public ProfesionalDeSaludController(ProfesionalDeSaludService profesionalService, ClinicaService clinicaService) {
        this.profesionalService = profesionalService;
        this.clinicaService = clinicaService;
    }

    public static class ProfesionalRequest {
        public String dominioSubdominio;
        public ProfesionalDeSalud profesional;
        public List<String> especialidades;
    }

    @PostMapping
    public ResponseEntity<ProfesionalDeSalud> crearProfesional(@RequestBody ProfesionalRequest request) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(request.dominioSubdominio);
        ProfesionalDeSalud nuevo = profesionalService.crearProfesional(request.profesional, request.especialidades,
                tenantId);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalDeSalud>> listarProfesionales(@RequestParam String dominioSubdominio) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        return ResponseEntity.ok(profesionalService.listarPorClinica(tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalDeSalud> actualizarProfesional(@PathVariable String id,
            @RequestBody ProfesionalRequest request) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(request.dominioSubdominio);
        ProfesionalDeSalud actualizado = profesionalService.actualizarProfesional(id, request.profesional,
                request.especialidades, tenantId);
        return ResponseEntity.ok(actualizado);
    }
}
