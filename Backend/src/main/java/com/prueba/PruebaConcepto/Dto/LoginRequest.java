package com.prueba.PruebaConcepto.Dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String cedula; // Changed from username to cedula to support both admin and professional
    private String password;
    private String dominioSubdominio;
}
