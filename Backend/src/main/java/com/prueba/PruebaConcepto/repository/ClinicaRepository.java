package com.prueba.PruebaConcepto.repository;



import com.prueba.PruebaConcepto.entity.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
    Optional<Clinica> findByDominioSubdominio(String dominioSubdominio);
    boolean existsByNombre(String nombre);
}

