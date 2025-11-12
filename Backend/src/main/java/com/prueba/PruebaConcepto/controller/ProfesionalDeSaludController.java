package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import com.prueba.PruebaConcepto.service.ProfesionalDeSaludService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesionales")
//@CrossOrigin(origins = "*")
public class ProfesionalDeSaludController {

    private final ProfesionalDeSaludService profesionalService;

    public ProfesionalDeSaludController(ProfesionalDeSaludService profesionalService) {
        this.profesionalService = profesionalService;
    }

    // DTO interno para recibir JSON
    public static class ProfesionalRequest {
        public ProfesionalDeSalud profesional;
        public List<String> especialidades;
    }

    @PostMapping("/{clinicaId}")
    public ResponseEntity<ProfesionalDeSalud> crearProfesional(
            @PathVariable String clinicaId,
            @RequestBody ProfesionalRequest request) {

        ProfesionalDeSalud nuevo = profesionalService.crearProfesional(
                clinicaId,
                request.profesional,
                request.especialidades
        );
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalDeSalud>> listarTodos() {
        return ResponseEntity.ok(profesionalService.listarTodos());
    }

    @GetMapping("/clinica/{clinicaId}")
    public ResponseEntity<List<ProfesionalDeSalud>> listarPorClinica(@PathVariable String clinicaId) {
        return ResponseEntity.ok(profesionalService.listarPorClinica(clinicaId));
    }
}