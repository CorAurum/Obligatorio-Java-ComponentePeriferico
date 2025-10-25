package com.prueba.PruebaConcepto.Dto;

public record ProfesionalDeSaludDto(
        Long idProfesional,
        String cedulaIdentidad,
        String nombre,
        String apellido,
        String numeroRegistro,
        String email,
        String telefono
) {}
