package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.service.ProfesionalDeSaludService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalDeSaludController {

    private final ProfesionalDeSaludService profesionalService;

    public ProfesionalDeSaludController(ProfesionalDeSaludService profesionalService) {
        this.profesionalService = profesionalService;
    }

    public static class ProfesionalRequest {
        public String tenantId;
        public ProfesionalDeSalud profesional;
        public List<String> especialidades;
    }

    @PostMapping
    public ResponseEntity<ProfesionalDeSalud> crearProfesional(@RequestBody ProfesionalRequest request) {
        ProfesionalDeSalud nuevo = profesionalService.crearProfesional(request.profesional, request.especialidades,
                request.tenantId);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalDeSalud>> listarProfesionales(@RequestParam String tenantId) {
        return ResponseEntity.ok(profesionalService.listarPorClinica(tenantId));
    }
}
