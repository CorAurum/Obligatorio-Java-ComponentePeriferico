package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    List<Administrador> findByClinicaId(Long clinicaId);
    boolean existsByEmail(String email);
}
