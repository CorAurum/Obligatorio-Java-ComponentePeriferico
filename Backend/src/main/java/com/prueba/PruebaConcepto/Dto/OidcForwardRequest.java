package com.prueba.PruebaConcepto.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for OIDC forward endpoint.
 * Received from Central Backend after gub.uy OIDC authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OidcForwardRequest {
    
    /**
     * The id_token from gub.uy OIDC
     */
    private String idToken;
    
    /**
     * The access_token from gub.uy OIDC
     */
    private String accessToken;
    
    /**
     * User's cedula (documento de identidad)
     */
    private String cedula;
    
    /**
     * User's email (if available)
     */
    private String email;
    
    /**
     * User's first name
     */
    private String primerNombre;
    
    /**
     * User's last name
     */
    private String primerApellido;
    
    /**
     * User's birth date (if available)
     */
    private String fechaNacimiento;
}

