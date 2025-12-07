package com.prueba.PruebaConcepto.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AuthProfileResponse {
    private String id;
    private String username;
    private String role;
    private String clinicaId;
    private String dominioSubdominio;
    private String cedula;
    private String displayName;
    private String email;
    private String telefono;
    private boolean activo;
    private List<EspecialidadSummary> especialidades;

    @Data
    @AllArgsConstructor
    public static class EspecialidadSummary {
        private String id;
        private String nombre;
    }
}
