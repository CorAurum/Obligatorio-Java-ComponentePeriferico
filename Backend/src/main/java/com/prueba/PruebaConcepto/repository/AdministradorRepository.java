package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    List<Administrador> findByClinicaId(String clinicaId);
    boolean existsByEmail(String email);
    Optional<Administrador> findByCedula(String cedula);}
