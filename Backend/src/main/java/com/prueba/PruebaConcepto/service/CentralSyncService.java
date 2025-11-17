package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.DocumentoCentralDTO;
import com.prueba.PruebaConcepto.Dto.ProfesionalCentralDTO;
import com.prueba.PruebaConcepto.Dto.UsuarioCentralDTO;
import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CentralSyncService {

    private final RestTemplate restTemplate;

    @Value("http://localhost:8080/CompC-1.0-SNAPSHOT")
    private String centralBaseUrl;

    public CentralSyncService() {
        this.restTemplate = new RestTemplate();
    }

    // 🔹 Enviar usuario al componente central
    public void enviarUsuarioAlCentral(UsuarioCentralDTO dto) {
        try {
            String url = centralBaseUrl + "/api/usuarios/externo";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UsuarioCentralDTO> request = new HttpEntity<>(dto, headers);
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (Exception e) {
            // Log the error but don't fail the operation - central sync is optional for local testing
            System.err.println("Warning: Could not sync user to central system: " + e.getMessage());
        }
    }

    // 🔹 Enviar documento clínico al componente central
    public void enviarDocumentoAlCentral(DocumentoCentralDTO dto) {
        try {
            String url = centralBaseUrl + "/api/documentoClinico/externo";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<DocumentoCentralDTO> request = new HttpEntity<>(dto, headers);
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (Exception e) {
            // Log the error but don't fail the operation - central sync is optional for local testing
            System.err.println("Warning: Could not sync document to central system: " + e.getMessage());
        }
    }

    // Registrar los profesionales de salud que creen en las clinicas del multi tenant en el central, para control de politicas de acceso etc
    public void enviarProfesionalAlCentral(ProfesionalCentralDTO dto, String centroId) {
        try {
            String url = centralBaseUrl + "/api/profesionales?centroId=" + centroId;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ProfesionalCentralDTO> request = new HttpEntity<>(dto, headers);
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (Exception e) {
            // Log the error but don't fail the operation - central sync is optional for local testing
            System.err.println("Warning: Could not sync professional to central system: " + e.getMessage());
        }
    }
}
