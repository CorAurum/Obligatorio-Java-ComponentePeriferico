package com.prueba.PruebaConcepto.Dto;

public record DocumentoMetadatoDto(
        Long idDocumento,
        String area,
        String fechaCreacion,
        String nombreProfesional,
        String idClinica,
        String cedulaUsuario
) {}
