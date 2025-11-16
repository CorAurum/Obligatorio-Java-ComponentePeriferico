package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.EstadoProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoProblemaRepository extends JpaRepository<EstadoProblema, Long> {
}
