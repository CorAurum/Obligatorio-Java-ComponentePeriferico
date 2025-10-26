package com.prueba.PruebaConcepto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prueba.PruebaConcepto.Dto.UsuarioDeSaludDto;
import com.prueba.PruebaConcepto.Dto.UsuarioDeSaludMetadatoDto;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.repository.UsuarioDeSaludRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@Service
public class UsuarioDeSaludService {

    private final UsuarioDeSaludRepository repo;

    public UsuarioDeSaludService(UsuarioDeSaludRepository repo) {
        this.repo = repo;
    }

    public UsuarioDeSaludDto crearUsuario(UsuarioDeSaludDto dto) {
        UsuarioDeSalud u = new UsuarioDeSalud(
                dto.cedulaIdentidad(),
                dto.email(),
                dto.nombre(),
                dto.apellido(),
                dto.direccion(),
                dto.telefono(),
                dto.fechaNacimiento(),
                LocalDate.now()
        );

        UsuarioDeSalud saved = repo.save(u);

        // 🔹 Llamar al metodo auxiliar para enviar el metadato al componente central y ahi guardar el usuario tambien
        enviarMetadato(saved);

        return toDto(saved);
    }

    public List<UsuarioDeSaludDto> listarUsuarios() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private UsuarioDeSaludDto toDto(UsuarioDeSalud u) {
        return new UsuarioDeSaludDto(
                u.getCedulaIdentidad(),
                u.getNombre(),
                u.getApellido(),
                u.getFechaNacimiento(),
                u.getEmail(),
                u.getTelefono(),
                u.getFechaRegistro(),
                u.getDireccion()
        );
    }


    // FUNCIONES PARA INTERACTUAR CON EL COMPONENTE CENTRAL
    // FUNCIONES PARA INTERACTUAR CON EL COMPONENTE CENTRAL
    private void enviarMetadato(UsuarioDeSalud usuario) {
        // URL del endpoint
        String url = "https://backend.web.elasticloud.uy/api/usuarioSalud/externo";

        // Crear el payload
        UsuarioDeSaludMetadatoDto metadato = new UsuarioDeSaludMetadatoDto(
                usuario.getCedulaIdentidad(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getFechaNacimiento(),
                usuario.getFechaRegistro()
        );

        try {
            // Configurar ObjectMapper para enviar fechas como strings ISO
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // permite serializar LocalDate y LocalDateTime
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // evita que se serialicen como arrays

            // Convertir DTO a JSON
            String jsonPayload = mapper.writeValueAsString(metadato);

            // LOG para ver exactamente qué se está enviando
            System.out.println("DEBUG JSON que se va a enviar al backend: " + jsonPayload);

            // Crear RestTemplate y headers
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construir la request con JSON como String
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            // Enviar la POST request
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Metadato de usuario enviado correctamente al endpoint externo.");
            } else {
                System.out.println("⚠️ No se pudo enviar el metadato. Código: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.err.println("❌ Error al enviar metadato del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
