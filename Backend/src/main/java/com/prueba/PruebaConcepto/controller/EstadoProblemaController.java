package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.EstadosProblemas;
import com.prueba.PruebaConcepto.repository.EstadosProblemasRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-problemas")
@CrossOrigin(origins = "*")
public class EstadoProblemaController {

    private final EstadosProblemasRepository repository;

    public EstadoProblemaController(EstadosProblemasRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/list")
    public List<EstadosProblemas> listarEstados() {
        return repository.findAll();
    }
}
