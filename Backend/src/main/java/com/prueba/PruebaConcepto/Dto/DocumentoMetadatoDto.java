package com.prueba.PruebaConcepto.Dto;

public record DocumentoMetadatoDto(
        Long idDocumento,
        String area,
        String fechaCreacion,
        String nombreProfesional,
        Long idClinica,
        String cedulaUsuario
) {}
