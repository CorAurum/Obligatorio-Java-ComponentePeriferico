package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.DocumentoCentralDTO;
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

    @Value("http://localhost:8080//CompC-1.0-SNAPSHOT")
    private String centralBaseUrl;

    public CentralSyncService() {
        this.restTemplate = new RestTemplate();
    }

    // 🔹 Enviar usuario al componente central
    public void enviarUsuarioAlCentral(UsuarioCentralDTO dto) {
        String url = centralBaseUrl + "/api/usuarios/externo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsuarioCentralDTO> request = new HttpEntity<>(dto, headers);
        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
    }

    // 🔹 Enviar documento clínico al componente central
    public void enviarDocumentoAlCentral(DocumentoCentralDTO dto) {
        String url = centralBaseUrl + "/api/documentoClinico/externo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DocumentoCentralDTO> request = new HttpEntity<>(dto, headers);
        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
    }
}
