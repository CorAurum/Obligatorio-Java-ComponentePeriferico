package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.ProfesionalDeSalud;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesionalDeSaludRepository extends JpaRepository<ProfesionalDeSalud, Long> {
    List<ProfesionalDeSalud> findByClinicaId(Long clinicaId);
    boolean existsByEmail(String email);
    boolean existsByCedulaIdentidad(String cedulaIdentidad);
}
