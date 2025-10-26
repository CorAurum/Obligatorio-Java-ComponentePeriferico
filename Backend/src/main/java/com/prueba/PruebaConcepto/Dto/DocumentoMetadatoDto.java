package com.prueba.PruebaConcepto.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocumentoMetadatoDto(

        @JsonProperty("idDocumentoOrigen")
        Long idDocumento,
        String area,
        String fechaCreacion,
        String nombreProfesional,
        @JsonProperty("CentroSaludId")
        Long idClinica,
        String cedulaUsuario
) {}
