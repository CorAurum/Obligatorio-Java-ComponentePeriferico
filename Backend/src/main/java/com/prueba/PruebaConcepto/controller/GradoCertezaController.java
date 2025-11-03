package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.GradosCerteza;
import com.prueba.PruebaConcepto.repository.GradosCertezaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grados-certeza")
@CrossOrigin(origins = "*")
public class GradoCertezaController {

    private final GradosCertezaRepository repository;

    public GradoCertezaController(GradosCertezaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/list")
    public List<GradosCerteza> listarGrados() {
        return repository.findAll();
    }
}
