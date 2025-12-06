package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.EstadoProblema;
import com.prueba.PruebaConcepto.entity.GradoCerteza;
import com.prueba.PruebaConcepto.entity.MotivoConsulta;
import com.prueba.PruebaConcepto.repository.EstadoProblemaRepository;
import com.prueba.PruebaConcepto.repository.GradoCertezaRepository;
import com.prueba.PruebaConcepto.repository.MotivoConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Exposes read-only catalog endpoints used by the frontend.
 * The dominioSubdominio parameter is accepted for compatibility with the proxy,
 * but is not used server-side because these catalogs are global/static.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogoClinicoController {

    private final MotivoConsultaRepository motivoConsultaRepository;
    private final GradoCertezaRepository gradoCertezaRepository;
    private final EstadoProblemaRepository estadoProblemaRepository;

    @GetMapping("/motivos-consulta")
    public ResponseEntity<List<MotivoConsulta>> listarMotivosConsulta(
            @RequestParam(value = "dominioSubdominio", required = false) String ignored
    ) {
        return ResponseEntity.ok(motivoConsultaRepository.findAll());
    }

    @GetMapping("/grados-certeza")
    public ResponseEntity<List<GradoCerteza>> listarGradosCerteza(
            @RequestParam(value = "dominioSubdominio", required = false) String ignored
    ) {
        return ResponseEntity.ok(gradoCertezaRepository.findAll());
    }

    @GetMapping("/estados-problema")
    public ResponseEntity<List<EstadoProblema>> listarEstadosProblema(
            @RequestParam(value = "dominioSubdominio", required = false) String ignored
    ) {
        return ResponseEntity.ok(estadoProblemaRepository.findAll());
    }
}

