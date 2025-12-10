package com.prueba.PruebaConcepto.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for OIDC forward endpoint.
 * Sent back to Central Backend with user information for redirect.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OidcForwardResponse {
    
    /**
     * Whether the lookup was successful
     */
    private boolean success;
    
    /**
     * Error message if success is false
     */
    private String error;
    
    /**
     * User's role: ADMINISTRADOR or PROFESIONAL
     */
    private String role;
    
    /**
     * User's ID in the peripheral system
     */
    private String userId;
    
    /**
     * User's cedula
     */
    private String cedula;
    
    /**
     * User's display name (nombre + apellido)
     */
    private String displayName;
    
    /**
     * The clinic's dominio/subdominio for multi-tenant routing
     */
    private String dominioSubdominio;
    
    /**
     * The original id_token (forwarded back for session)
     */
    private String idToken;
    
    /**
     * The original access_token (forwarded back for session)
     */
    private String accessToken;
    
    /**
     * Helper method to create an error response
     */
    public static OidcForwardResponse error(String message) {
        return OidcForwardResponse.builder()
                .success(false)
                .error(message)
                .build();
    }
}

