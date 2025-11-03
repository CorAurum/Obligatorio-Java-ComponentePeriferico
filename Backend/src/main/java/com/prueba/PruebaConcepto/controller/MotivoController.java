package com.prueba.PruebaConcepto.controller;


import com.prueba.PruebaConcepto.entity.Motivos;
import com.prueba.PruebaConcepto.repository.MotivosRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motivos")
@CrossOrigin(origins = "*") // por si lo necesitás luego para el central
public class MotivoController {

    private final MotivosRepository motivoRepository;

    public MotivoController(MotivosRepository motivoRepository) {
        this.motivoRepository = motivoRepository;
    }

    @GetMapping("/buscar")
    public List<Motivos> buscarMotivos(@RequestParam("q") String query) {
        return motivoRepository.findTop10ByMotivoContainingIgnoreCase(query);
    }
}
