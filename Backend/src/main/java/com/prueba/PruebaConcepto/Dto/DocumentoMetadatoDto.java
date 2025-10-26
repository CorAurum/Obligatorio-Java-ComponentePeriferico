package com.prueba.PruebaConcepto.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocumentoMetadatoDto(

        @JsonProperty("idDocumentoOrigen")
        Long idDocumento,
        String area,
        String fechaCreacion,
        String nombreProfesional,
        String idClinica,
        String cedulaUsuario
) {}
