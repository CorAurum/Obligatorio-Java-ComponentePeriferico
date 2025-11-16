package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.service.ClinicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinicas")
//@CrossOrigin(origins = "*")
public class ClinicaController {

    private final ClinicaService clinicaService;

    public ClinicaController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    @PostMapping
    public ResponseEntity<Clinica> crearClinica(@RequestBody Clinica clinica) {
        Clinica nueva = clinicaService.crearClinica(clinica);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping
    public ResponseEntity<List<Clinica>> listarClinicas() {
        return ResponseEntity.ok(clinicaService.listarClinicas());
    }

    @GetMapping("/{dominio}")
    public ResponseEntity<Clinica> obtenerPorDominio(@PathVariable String dominio) {
        return clinicaService.obtenerPorDominio(dominio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
