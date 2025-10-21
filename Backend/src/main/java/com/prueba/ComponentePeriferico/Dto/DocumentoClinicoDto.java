package com.prueba.PruebaConcepto.Dto;

import java.time.LocalDateTime;
import java.util.List;

public record DocumentoClinicoDto(
        Long idDocumento,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaProximaConsultaRecomendada,
        LocalDateTime fechaProximaConsultaConfirmada,
        String areaProximoControl,
        List<MotivoConsultaDto> motivosConsulta,
        List<DiagnosticoDto> diagnosticos,
        Long idClinica,
        String cedulaUsuario,
        Long idProfesional
) {}
