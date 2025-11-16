package com.prueba.PruebaConcepto.repository;


import com.prueba.PruebaConcepto.entity.IdentificadorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdentificadorUsuarioRepository extends JpaRepository<IdentificadorUsuario, Long> {
    List<IdentificadorUsuario> findByUsuarioId(Long usuarioId);
}
