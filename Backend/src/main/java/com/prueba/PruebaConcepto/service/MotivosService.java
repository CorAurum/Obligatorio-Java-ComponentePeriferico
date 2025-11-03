package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.entity.Motivos;
import com.prueba.PruebaConcepto.repository.MotivosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotivosService {

    private final MotivosRepository motivosRepository;

    public MotivosService(MotivosRepository motivosRepository) {
        this.motivosRepository = motivosRepository;
    }

    public List<Motivos> listarTodos() {
        return motivosRepository.findAll();
    }

    public List<Motivos> buscarPorTexto(String texto) {
        // Idealmente implementás un query personalizado en el repository
        return motivosRepository.findByMotivoContainingIgnoreCase(texto);
    }

    public Optional<Motivos> obtenerPorId(Long id) {
        return motivosRepository.findById(id);
    }
}
