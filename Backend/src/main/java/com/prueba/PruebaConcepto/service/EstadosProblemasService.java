package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.EstadosProblemas;
import com.prueba.PruebaConcepto.repository.EstadosProblemasRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadosProblemasService {

    private final EstadosProblemasRepository estadosProblemasRepository;

    public EstadosProblemasService(EstadosProblemasRepository estadosProblemasRepository) {
        this.estadosProblemasRepository = estadosProblemasRepository;
    }

    public List<EstadosProblemas> listarTodos() {
        return estadosProblemasRepository.findAll();
    }

    public Optional<EstadosProblemas> obtenerPorId(Long id) {
        return estadosProblemasRepository.findById(id);
    }
}
