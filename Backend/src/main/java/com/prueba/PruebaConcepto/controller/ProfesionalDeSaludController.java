package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.ProfesionalDeSaludDto;
import com.prueba.PruebaConcepto.service.ProfesionalDeSaludService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesionales")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfesionalDeSaludController {

    private final ProfesionalDeSaludService service;

    public ProfesionalDeSaludController(ProfesionalDeSaludService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<ProfesionalDeSaludDto> add(@RequestBody ProfesionalDeSaludDto dto) {
        return ResponseEntity.ok(service.crearProfesional(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProfesionalDeSaludDto>> list() {
        return ResponseEntity.ok(service.listarProfesionales());
    }
}
