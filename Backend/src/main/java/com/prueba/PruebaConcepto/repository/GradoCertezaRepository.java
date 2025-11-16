package com.prueba.PruebaConcepto.repository;


import com.prueba.PruebaConcepto.entity.GradoCerteza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradoCertezaRepository extends JpaRepository<GradoCerteza, Long> {
}
