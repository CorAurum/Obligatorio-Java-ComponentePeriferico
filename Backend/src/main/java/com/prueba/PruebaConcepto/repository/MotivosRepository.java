package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.Motivos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotivosRepository extends JpaRepository<Motivos, Long> {

    List<Motivos> findTop10ByMotivoContainingIgnoreCase(String motivo);
    List<Motivos> findByMotivoContainingIgnoreCase(String texto);
}
