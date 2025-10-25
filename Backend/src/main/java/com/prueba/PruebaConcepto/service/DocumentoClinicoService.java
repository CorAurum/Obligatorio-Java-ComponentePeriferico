package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.*;
import com.prueba.PruebaConcepto.entity.*;
import com.prueba.PruebaConcepto.repository.*;
import org.springframework.stereotype.Service;



import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.format.DateTimeFormatter;


@Service
public class DocumentoClinicoService {

    private final DocumentoClinicoRepository docRepo;
    private final ClinicaRepository clinicaRepo;
    private final UsuarioDeSaludRepository usuarioRepo;
    private final ProfesionalDeSaludRepository profesionalRepo;

    public DocumentoClinicoService(DocumentoClinicoRepository docRepo,
                                   ClinicaRepository clinicaRepo,
                                   UsuarioDeSaludRepository usuarioRepo,
                                   ProfesionalDeSaludRepository profesionalRepo) {
        this.docRepo = docRepo;
        this.clinicaRepo = clinicaRepo;
        this.usuarioRepo = usuarioRepo;
        this.profesionalRepo = profesionalRepo;
    }

    public DocumentoClinicoDto crearDocumento(DocumentoClinicoDto dto) {
        Clinica clinica = clinicaRepo.findById(dto.idClinica())
                .orElseThrow(() -> new RuntimeException("Clinica no encontrada"));
        UsuarioDeSalud usuario = usuarioRepo.findByCedulaIdentidad(dto.cedulaUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ProfesionalDeSalud prof = profesionalRepo.findById(dto.idProfesional())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        DocumentoClinico doc = new DocumentoClinico();
        doc.setFechaCreacion(LocalDateTime.now());
        doc.setFechaProximaConsultaRecomendada(dto.fechaProximaConsultaRecomendada()); //cambiar
        doc.setFechaProximaConsultaConfirmada(dto.fechaProximaConsultaConfirmada()); //cambiar
        doc.setAreaProximoControl(dto.areaProximoControl());
        doc.setClinica(clinica);
        doc.setUsuario(usuario);
        doc.setProfesional(prof);
        doc.setArea(dto.area());

        // motivos
        doc.setMotivosConsulta(dto.motivosConsulta().stream()
                .map(m -> {
                    MotivoConsulta mc = new MotivoConsulta();
                    mc.setMotivo(m.motivo());
                    mc.setDocumento(doc);
                    return mc;
                }).toList());

        // diagnosticos
        doc.setDiagnosticos(dto.diagnosticos().stream()
                .map(d -> {
                    Diagnostico diag = new Diagnostico();
                    diag.setDescripcion(d.descripcion());
                    diag.setFechaInicio(d.fechaInicio());
                    diag.setEstado(d.estado());
                    diag.setGradoCerteza(d.gradoCerteza());
                    diag.setDocumento(doc);
                    return diag;
                }).toList());

        DocumentoClinico saved = docRepo.save(doc);


        // 🔹 Llamar al metodo auxiliar para enviar el metadato al componente central y que sea registrado
        enviarMetadato(saved);


        return toDto(saved);
    }



    public void eliminarDocumento(Long id) {
        if (!docRepo.existsById(id)) throw new RuntimeException("Documento no encontrado");
        docRepo.deleteById(id);
    }

    public List<DocumentoClinicoDto> listarTodos() {
        return docRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private DocumentoClinicoDto toDto(DocumentoClinico d) {
        return new DocumentoClinicoDto(
                d.getIdDocumento(),
                d.getArea(),
                d.getFechaCreacion(),
                d.getFechaProximaConsultaRecomendada(),
                d.getFechaProximaConsultaConfirmada(),
                d.getAreaProximoControl(),

                d.getMotivosConsulta().stream()
                        .map(m -> new MotivoConsultaDto(m.getId(), m.getMotivo()))
                        .toList(),
                d.getDiagnosticos().stream()
                        .map(di -> new DiagnosticoDto(di.getId(), di.getDescripcion(),
                                di.getFechaInicio(), di.getEstado(), di.getGradoCerteza()))
                        .toList(),
                d.getClinica() != null ? d.getClinica().getId() : null,
                d.getUsuario() != null ? d.getUsuario().getCedulaIdentidad() : null,
                d.getProfesional() != null ? d.getProfesional().getIdProfesional() : null

        );
    }


    // FUNCIONES RELACIONADAS AL COMPONENTE CENTRAL


    private void enviarMetadato(DocumentoClinico documento) {
        // URL del endpoint al que vas a enviar el metadato
        String url = "https://tu-endpoint-x/api/metadatos"; // ⚠️ cámbiala por la real

        // Crear el payload
        DocumentoMetadatoDto metadato = new DocumentoMetadatoDto(
                documento.getIdDocumento(),
                documento.getArea(),
                documento.getFechaCreacion().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                documento.getProfesional().getNombre(),
                documento.getClinica().getId(),
                documento.getUsuario().getCedulaIdentidad()
        );

        // Crear el RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Crear los headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear la request
        HttpEntity<DocumentoMetadatoDto> request = new HttpEntity<>(metadato, headers);

        try {
            // Enviar la POST request
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Metadato enviado correctamente al endpoint externo.");
            } else {
                System.out.println("⚠️ No se pudo enviar el metadato. Código: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("❌ Error al enviar metadato: " + e.getMessage());
        }
    }




}
