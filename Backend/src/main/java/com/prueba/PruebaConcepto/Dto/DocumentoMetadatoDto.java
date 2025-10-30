package com.prueba.PruebaConcepto.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocumentoMetadatoDto(

        @JsonProperty("idDocumentoOrigen")
        Long idDocumento,
        Long IdProfesional,
        String IdUsuario,
        @JsonProperty("centroSaludId")
        Long idClinica,
        String area,
        @JsonProperty("usuarioDeSaludId")
        String cedulaUsuario
) {}
