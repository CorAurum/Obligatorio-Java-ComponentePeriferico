package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
}
