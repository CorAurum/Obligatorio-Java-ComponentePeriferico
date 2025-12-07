package com.prueba.PruebaConcepto.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private String jwt;       // Preferred field for the access token
    private String token;     // Legacy alias kept for backward compatibility
    private String refreshToken;
    private String type = "Bearer";
    private String id; // Changed to String to support both Long (admin) and String (professional) IDs
    private String username;
    private String role;
    private String clinicaId;
    private String dominioSubdominio;

    public LoginResponse(
            String jwt,
            String refreshToken,
            String type,
            String id,
            String username,
            String role,
            String clinicaId,
            String dominioSubdominio
    ) {
        this.jwt = jwt;
        this.token = jwt; // Keep old field name populated to avoid breaking older clients
        this.refreshToken = refreshToken;
        this.type = type != null ? type : "Bearer";
        this.id = id;
        this.username = username;
        this.role = role;
        this.clinicaId = clinicaId;
        this.dominioSubdominio = dominioSubdominio;
    }
}
