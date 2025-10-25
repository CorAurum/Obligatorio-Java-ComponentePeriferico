package com.prueba.PruebaConcepto.Dto;

import java.time.LocalDate;

public record UsuarioDeSaludDto(
        String cedulaIdentidad,
        String nombre,
        String apellido,
        LocalDate fechaNacimiento,
        String email,
        String telefono,
        LocalDate fechaRegistro,
        String direccion
) {}
