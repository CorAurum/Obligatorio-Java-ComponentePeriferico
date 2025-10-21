package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioDeSaludRepository extends JpaRepository<UsuarioDeSalud, String> {
    Optional<UsuarioDeSalud> findByCedulaIdentidad(String cedulaIdentidad);
}
