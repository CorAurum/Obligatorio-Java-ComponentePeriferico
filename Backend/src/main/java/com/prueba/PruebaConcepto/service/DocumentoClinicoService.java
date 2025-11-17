package com.prueba.PruebaConcepto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prueba.PruebaConcepto.Dto.DocumentoCentralDTO;
import com.prueba.PruebaConcepto.Dto.DocumentoClinicoParaUsuarioDTO;
import com.prueba.PruebaConcepto.Dto.DocumentoMapper;
import com.prueba.PruebaConcepto.entity.*;
import com.prueba.PruebaConcepto.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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


    @Value("${periferico.public.base-url}")
    private String perifericoPublicBaseUrl;


    // CREA EL DOCUMENTO CLINICO
    @Transactional
    public DocumentoClinico crearDocumento(String idUsuario, String idProfesional, DocumentoClinico documento,
                                           String tenantId) {
        Clinica clinica = clinicaRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + tenantId));

        UsuarioDeSalud usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + idUsuario));

        ProfesionalDeSalud profesional = profesionalRepository.findById(idProfesional)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado con ID: " + idProfesional));

        documento.setUsuario(usuario);
        documento.setProfesional(profesional);
        documento.setClinica(clinica);

        // Motivos (resolución a entidades existentes)
        if (documento.getMotivosConsulta() != null) {
            documento.setMotivosConsulta(
                    documento.getMotivosConsulta().stream()
                            .map(m -> motivoRepository.findById(m.getId())
                                    .orElseThrow(() -> new IllegalArgumentException(
                                            "Motivo no encontrado con ID: " + m.getId())))
                            .toList());
        }

        // Diagnósticos (resolver gradoCerteza y estadoProblema)
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

        // Guardar localmente
        DocumentoClinico nuevoDoc = documentoRepository.save(documento);

        // --- Construir la URL pública que el central usará para pedir el documento completo ---
        // Se arma usando la base configurada en application.properties: periferico.public.base-url
        // Ejemplo resultante: http://Periferico.backend.com/api/documentos/{id}/detalle
        String urlDetalle = perifericoPublicBaseUrl;
        if (!urlDetalle.endsWith("/")) urlDetalle += "/";
        urlDetalle += "api/documentos/" + nuevoDoc.getId() + "/detalle";

        // --- Mapear a DTO para enviar al central (metadatos) ---
        DocumentoCentralDTO dto = documentoMapper.toCentralDTO(nuevoDoc, String.valueOf(clinica.getId()));
        // Sobreescribimos/establecemos la urlAlojamiento con la URL de detalle (no es un enlace a almacenamiento)
        dto.setUrlAlojamiento(urlDetalle);

        try {
            // Opcional: show JSON of DTO for debugging (ya tenías esta impresión)
            System.out.println("\nJSON ENVIADO AL CENTRAL (Documento Clínico):");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));

            // Enviar al central
            centralSyncService.enviarDocumentoAlCentral(dto);
        } catch (Exception ex) {
            // Logueamos el error: no provocamos rollback, se mantiene el documento local.
            System.err.println("No se pudo sincronizar documento con central: " + ex.getMessage());
        }

        return nuevoDoc;
    }

    // MAPEA EL DOCUMENTO PARA SER DEVUELTO AL CENTRAL Y LEIDO POR EL USUARIO

    public DocumentoClinicoParaUsuarioDTO mapDocumentoAParaUsuarioDTO(DocumentoClinico doc) {
        DocumentoClinicoParaUsuarioDTO out = new DocumentoClinicoParaUsuarioDTO();

        // Tenant -> nombre de la clínica
        Clinica clinica = doc.getClinica(); // o doc.getClinica() dependiendo del nombre de campo
        // Si en tu entidad el nombre del getter es getClinica() o getCentroDeSalud(), adaptá:
        if (clinica == null) {
            // intentar obtener desde documento.getClinicaId() o lanzar excepción
            out.setTenant("Desconocido");
        } else {
            out.setTenant(clinica.getNombre());
        }

        // Profesional -> "Nombre Apellido"
        ProfesionalDeSalud p = doc.getProfesional();
        String profesionalNombre = p != null ? (p.getNombre() + " " + p.getApellido()).trim() : null;
        out.setProfesional(profesionalNombre);

        // Documento interno
        DocumentoClinicoParaUsuarioDTO.DocumentoDTO documentoDto = new DocumentoClinicoParaUsuarioDTO.DocumentoDTO();
        documentoDto.setArea(doc.getArea());
        documentoDto.setTitulo(doc.getTitulo());
        documentoDto.setDescripcion(doc.getDescripcion());
        documentoDto.setTipoDocumento(doc.getTipoDocumento());
        documentoDto.setFechaCreacion(LocalDateTime.parse(doc.getFechaCreacion()));
        documentoDto.setFechaProximaConsultaConfirmada(doc.getFechaProximaConsultaConfirmada());

        // Motivos: lista de nombres
        if (doc.getMotivosConsulta() != null) {
            List<String> motivosNombres = doc.getMotivosConsulta()
                    .stream()
                    .map(m -> m.getMotivo()) // asumo getNombre() en Motivo entity
                    .toList();
            documentoDto.setMotivosConsulta(motivosNombres);
        }

        // Diagnosticos: obtener nombres de grado y estado
        if (doc.getDiagnosticos() != null) {
            List<DocumentoClinicoParaUsuarioDTO.DiagnosticoDTO> diagDtos =
                    doc.getDiagnosticos().stream().map(d -> {
                        DocumentoClinicoParaUsuarioDTO.DiagnosticoDTO dd = new DocumentoClinicoParaUsuarioDTO.DiagnosticoDTO();
                        // grado de certeza -> nombre
                        dd.setGradoCerteza(d.getGradoCerteza() != null ? d.getGradoCerteza().getNombre() : null);
                        // estado problema -> nombre
                        dd.setEstadoProblema(d.getEstadoProblema() != null ? d.getEstadoProblema().getNombre() : null);
                        dd.setDescripcion(d.getDescripcion());
                        return dd;
                    }).toList();
            documentoDto.setDiagnosticos(diagDtos);
        }

        out.setDocumento(documentoDto);
        return out;
    }


    public List<DocumentoClinico> listarPorClinica(String tenantId) {
        return documentoRepository.findByClinicaId(tenantId);
    }

    public List<DocumentoClinico> listarPorUsuarioYClinica(Long usuarioId, String tenantId) {
        return documentoRepository.findByUsuarioIdAndClinicaId(usuarioId, tenantId);
    }

    public List<DocumentoClinico> listarPorProfesionalYClinica(String profesionalId, String tenantId) {
        return documentoRepository.findByProfesional_IdProfesionalAndClinicaId(profesionalId, tenantId);
    }

    public DocumentoClinico listarPorIdYClinica(Long id, String tenantId) {
        return documentoRepository.findByIdAndClinicaId(id, tenantId).orElse(null);
    }

    public DocumentoClinico listarPorId(Long id) {
        return documentoRepository.findById(id).orElse(null);
    }



}
