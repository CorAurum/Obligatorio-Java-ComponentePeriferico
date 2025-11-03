package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;


@Repository
public interface UsuarioDeSaludRepository extends JpaRepository<UsuarioDeSalud, String> {
    List<UsuarioDeSalud> findByClinicaId(Long clinicaId);
    Optional<UsuarioDeSalud> findByEmail(String email);
}