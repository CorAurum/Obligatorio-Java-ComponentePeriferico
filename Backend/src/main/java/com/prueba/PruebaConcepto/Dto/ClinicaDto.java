package com.prueba.PruebaConcepto.Dto;

public record ClinicaDto(
        Long id,
        String codigoClinica,
        String nombre,
        String direccion,
        String telefono,
        String tipoInstitucion
) {}
