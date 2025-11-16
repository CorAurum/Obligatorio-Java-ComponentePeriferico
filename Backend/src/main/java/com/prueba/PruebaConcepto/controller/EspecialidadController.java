package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.Especialidad;
import com.prueba.PruebaConcepto.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    // POST - Crear especialidad
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Especialidad esp) {

        if (especialidadRepository.existsById(esp.getId())) {
            return ResponseEntity.ok("Ya existía en periférico.");
        }

        especialidadRepository.save(esp);
        return ResponseEntity.status(201).body(esp);
    }

    // Listar todas las especialidades
    @GetMapping
    public ResponseEntity<List<Especialidad>> listarTodas() {
        List<Especialidad> lista = especialidadRepository.findAll();
        return ResponseEntity.ok(lista);
    }

    // Obtener una especialidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable String id) {
        Optional<Especialidad> esp = especialidadRepository.findById(id);

        if (esp.isEmpty()) {
            return ResponseEntity.status(404)
                    .body("No existe una especialidad con ID: " + id);
        }

        return ResponseEntity.ok(esp.get());
    }
}
