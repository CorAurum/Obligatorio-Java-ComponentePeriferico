package com.prueba.PruebaConcepto.Dto;

import java.time.LocalDate;

public record UsuarioDeSaludMetadatoDto(
        String cedulaIdentidad,
        String email,
        String nombre,
        String apellido,
        String direccion,
        String telefono,
        LocalDate fechaNacimiento,
        LocalDate fechaRegistro
) {}
