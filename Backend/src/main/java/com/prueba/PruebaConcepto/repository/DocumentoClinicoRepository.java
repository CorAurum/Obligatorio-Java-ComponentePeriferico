package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoClinicoRepository extends JpaRepository<DocumentoClinico, Long> {
    // listar documentos por clínica
    //List<DocumentoClinico> findByClinica_Id(Long id);

    // listar documentos por usuario (cédula de identidad)
    //List<DocumentoClinico> findByUsuario_CedulaIdentidad(String cedulaUsuario);
}
