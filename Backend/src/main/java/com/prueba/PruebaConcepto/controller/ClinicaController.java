package com.prueba.PruebaConcepto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.service.ClinicaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//a
@RestController
@RequestMapping("/api/clinicas")
//@CrossOrigin(origins = "*")
public class ClinicaController {

    private static final Logger logger = LoggerFactory.getLogger(ClinicaController.class);
    private final ClinicaService clinicaService;
    private final ObjectMapper objectMapper;


    public ClinicaController(ClinicaService clinicaService, ObjectMapper objectMapper) {
        this.clinicaService = clinicaService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<Clinica> crearClinica(@RequestBody Clinica clinica) {
        try {
            // Log the full JSON received
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(clinica);
            logger.info("📥 JSON recibido en /api/clinicas:\n{}", json);
        } catch (Exception e) {
            logger.error("Error al convertir Clinica a JSON para logging", e);
        }

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
