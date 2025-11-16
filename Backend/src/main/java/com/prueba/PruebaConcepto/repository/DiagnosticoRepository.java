package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
}
