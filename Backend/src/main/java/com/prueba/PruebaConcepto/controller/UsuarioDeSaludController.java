package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.UsuarioDeSaludDto;
import com.prueba.PruebaConcepto.service.UsuarioDeSaludService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios-salud")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioDeSaludController {

    private final UsuarioDeSaludService service;

    public UsuarioDeSaludController(UsuarioDeSaludService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<UsuarioDeSaludDto> add(@RequestBody UsuarioDeSaludDto dto) {
        return ResponseEntity.ok(service.crearUsuario(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UsuarioDeSaludDto>> list() {
        return ResponseEntity.ok(service.listarUsuarios());
    }
}
