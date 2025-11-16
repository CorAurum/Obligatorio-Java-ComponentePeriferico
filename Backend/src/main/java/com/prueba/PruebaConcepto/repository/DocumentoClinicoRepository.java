package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoClinicoRepository extends JpaRepository<DocumentoClinico, Long> {
    List<DocumentoClinico> findByUsuarioId(Long id);

    List<DocumentoClinico> findByProfesional_IdProfesional(Long idProfesional);

    public DocumentoClinico findByid(Long id);

    List<DocumentoClinico> findByClinicaId(String clinicaId);

    List<DocumentoClinico> findByUsuarioIdAndClinicaId(Long usuarioId, String clinicaId);

    List<DocumentoClinico> findByProfesional_IdProfesionalAndClinicaId(Long idProfesional, String clinicaId);

    Optional<DocumentoClinico> findByIdAndClinicaId(Long id, String clinicaId);
}
