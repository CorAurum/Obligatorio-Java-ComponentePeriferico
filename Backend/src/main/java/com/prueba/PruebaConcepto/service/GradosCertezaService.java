package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.GradosCerteza;
import com.prueba.PruebaConcepto.repository.GradosCertezaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradosCertezaService {

    private final GradosCertezaRepository gradosCertezaRepository;

    public GradosCertezaService(GradosCertezaRepository gradosCertezaRepository) {
        this.gradosCertezaRepository = gradosCertezaRepository;
    }

    public List<GradosCerteza> listarTodos() {
        return gradosCertezaRepository.findAll();
    }

    public Optional<GradosCerteza> obtenerPorId(Long id) {
        return gradosCertezaRepository.findById(id);
    }
}
