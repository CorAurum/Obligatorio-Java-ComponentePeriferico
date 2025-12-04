package com.prueba.PruebaConcepto.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String id; // Changed to String to support both Long (admin) and String (professional) IDs
    private String username;
    private String role;
    private String clinicaId;
}
