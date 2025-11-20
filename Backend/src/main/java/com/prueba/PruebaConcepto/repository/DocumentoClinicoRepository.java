package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoClinicoRepository extends JpaRepository<DocumentoClinico, String> {
    List<DocumentoClinico> findByUsuarioId(Long id);

    List<DocumentoClinico> findByProfesional_IdProfesional(String idProfesional);

    public DocumentoClinico findByid(String id);

    List<DocumentoClinico> findByClinicaId(String clinicaId);

    List<DocumentoClinico> findByUsuarioIdAndClinicaId(Long usuarioId, String clinicaId);

    List<DocumentoClinico> findByProfesional_IdProfesionalAndClinicaId(String idProfesional, String clinicaId);

    Optional<DocumentoClinico> findByIdAndClinicaId(String id, String clinicaId);

    @Query("""
    SELECT d FROM DocumentoClinico d
    WHERE d.usuario.id = :usuarioId
      AND d.clinica.id = :clinicaId
""")
    List<DocumentoClinico> listarPorUsuarioYClinica(
            @Param("usuarioId") Long usuarioId,
            @Param("clinicaId") String clinicaId
    );
}
