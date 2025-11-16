package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.MotivoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivoConsultaRepository extends JpaRepository<MotivoConsulta, Long> {
}
