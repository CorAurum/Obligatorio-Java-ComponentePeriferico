package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.Personalizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalizacionRepository extends JpaRepository<Personalizacion, Long> {

    // Permite obtener la personalización por clínica
    Optional<Personalizacion> findByClinica_Id(String clinicaId);
}
