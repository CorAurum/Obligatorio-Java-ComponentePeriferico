package com.prueba.PruebaConcepto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prueba.PruebaConcepto.Dto.DocumentoCentralDTO;
import com.prueba.PruebaConcepto.Dto.DocumentoMapper;
import com.prueba.PruebaConcepto.entity.*;
import com.prueba.PruebaConcepto.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DocumentoClinicoService {

    private final DocumentoClinicoRepository documentoRepository;
    private final UsuarioDeSaludRepository usuarioRepository;
    private final ProfesionalDeSaludRepository profesionalRepository;
    private final MotivoConsultaRepository motivoRepository;
    private final GradoCertezaRepository gradoCertezaRepository;
    private final EstadoProblemaRepository estadoProblemaRepository;
    private final ClinicaRepository clinicaRepository;
    private final CentralSyncService centralSyncService;
    private final DocumentoMapper documentoMapper;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public DocumentoClinicoService(
            DocumentoClinicoRepository documentoRepository,
            UsuarioDeSaludRepository usuarioRepository,
            ProfesionalDeSaludRepository profesionalRepository,
            MotivoConsultaRepository motivoRepository,
            GradoCertezaRepository gradoCertezaRepository,
            EstadoProblemaRepository estadoProblemaRepository,
            ClinicaRepository clinicaRepository,
            CentralSyncService centralSyncService,
            DocumentoMapper documentoMapper) {

        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.profesionalRepository = profesionalRepository;
        this.motivoRepository = motivoRepository;
        this.gradoCertezaRepository = gradoCertezaRepository;
        this.estadoProblemaRepository = estadoProblemaRepository;
        this.clinicaRepository = clinicaRepository;
        this.centralSyncService = centralSyncService;
        this.documentoMapper = documentoMapper;
    }

    @Transactional
    public DocumentoClinico crearDocumento(String idUsuario, Long idProfesional, DocumentoClinico documento,
            String tenantId) {
        Clinica clinica = clinicaRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + tenantId));

        UsuarioDeSalud usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario));

        ProfesionalDeSalud profesional = profesionalRepository.findById(idProfesional)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado con ID: " + idProfesional));

        documento.setUsuario(usuario);
        documento.setProfesional(profesional);

        // Motivos
        if (documento.getMotivosConsulta() != null) {
            documento.setMotivosConsulta(
                    documento.getMotivosConsulta().stream()
                            .map(m -> motivoRepository.findById(m.getId())
                                    .orElseThrow(() -> new IllegalArgumentException(
                                            "Motivo no encontrado con ID: " + m.getId())))
                            .toList());
        }

        // Diagnósticos
        if (documento.getDiagnosticos() != null) {
            for (Diagnostico d : documento.getDiagnosticos()) {
                d.setDocumentoClinico(documento);
                d.setGradoCerteza(gradoCertezaRepository.findById(d.getGradoCerteza().getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Grado de certeza no encontrado con ID: " + d.getGradoCerteza().getId())));
                d.setEstadoProblema(estadoProblemaRepository.findById(d.getEstadoProblema().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Estado de problema no encontrado")));
            }
        }

        DocumentoClinico nuevoDoc = documentoRepository.save(documento);

        DocumentoCentralDTO dto = documentoMapper.toCentralDTO(nuevoDoc, String.valueOf(clinica.getId()));

        try {
            System.out.println("\nJSON ENVIADO AL CENTRAL (Documento Clínico):");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
        } catch (Exception e) {
            System.out.println("Error mostrando JSON del documento: " + e.getMessage());
        }

        centralSyncService.enviarDocumentoAlCentral(dto);
        return nuevoDoc;
    }

    public List<DocumentoClinico> listarPorClinica(String tenantId) {
        return documentoRepository.findByClinicaId(tenantId);
    }

    public List<DocumentoClinico> listarPorUsuarioYClinica(Long usuarioId, String tenantId) {
        return documentoRepository.findByUsuarioIdAndClinicaId(usuarioId, tenantId);
    }

    public List<DocumentoClinico> listarPorProfesionalYClinica(Long profesionalId, String tenantId) {
        return documentoRepository.findByProfesional_IdProfesionalAndClinicaId(profesionalId, tenantId);
    }

    public DocumentoClinico listarPorIdYClinica(Long id, String tenantId) {
        return documentoRepository.findByIdAndClinicaId(id, tenantId).orElse(null);
    }
}
