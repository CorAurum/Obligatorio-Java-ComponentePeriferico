package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.List;

@Repository
public interface DocumentoClinicoRepository extends JpaRepository<DocumentoClinico, Long> {
    List<DocumentoClinico> findByUsuarioId(Long id);
    List<DocumentoClinico> findByProfesional_IdProfesional(Long idProfesional);
}
