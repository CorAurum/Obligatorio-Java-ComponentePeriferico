package com.prueba.PruebaConcepto.Dto;

import java.time.LocalDate;

public record DiagnosticoDto(
        Long id,
        String descripcion,
        LocalDate fechaInicio,
        String estado,
        String gradoCerteza
) {}
