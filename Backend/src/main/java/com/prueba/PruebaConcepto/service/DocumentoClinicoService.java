package com.prueba.PruebaConcepto.service;

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

    public DocumentoClinicoService(
            DocumentoClinicoRepository documentoRepository,
            UsuarioDeSaludRepository usuarioRepository,
            ProfesionalDeSaludRepository profesionalRepository,
            MotivoConsultaRepository motivoRepository,
            GradoCertezaRepository gradoCertezaRepository,
            EstadoProblemaRepository estadoProblemaRepository) {

        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.profesionalRepository = profesionalRepository;
        this.motivoRepository = motivoRepository;
        this.gradoCertezaRepository = gradoCertezaRepository;
        this.estadoProblemaRepository = estadoProblemaRepository;
    }

    @Transactional
    public DocumentoClinico crearDocumento(Long idClinica, String idUsuario, Long idProfesional, DocumentoClinico documento) {
        // Buscar usuario y profesional
        UsuarioDeSalud usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con cédula/email: " + idUsuario));

        ProfesionalDeSalud profesional = profesionalRepository.findById(idProfesional)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado con ID: " + idProfesional));

        documento.setUsuario(usuario);
        documento.setProfesional(profesional);

        // Asignar motivos existentes
        if (documento.getMotivosConsulta() != null) {
            documento.setMotivosConsulta(
                    documento.getMotivosConsulta().stream()
                            .map(m -> motivoRepository.findById(m.getId())
                                    .orElseThrow(() -> new IllegalArgumentException("Motivo no encontrado con ID: " + m.getId())))
                            .toList()
            );
        }

        // Asignar diagnósticos (y enlazarlos al documento)
        if (documento.getDiagnosticos() != null) {
            for (Diagnostico d : documento.getDiagnosticos()) {
                d.setDocumentoClinico(documento);
                d.setGradoCerteza(gradoCertezaRepository.findById(d.getGradoCerteza().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Grado de certeza no encontrado")));
                d.setEstadoProblema(estadoProblemaRepository.findById(d.getEstadoProblema().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Estado de problema no encontrado")));
            }
        }

        return documentoRepository.save(documento);
    }

    public List<DocumentoClinico> listarPorUsuario(Long usuarioId) {
        return documentoRepository.findByUsuarioId(usuarioId);
    }

    public List<DocumentoClinico> listarPorProfesional(Long profesionalId) {
        return documentoRepository.findByProfesional_IdProfesional(profesionalId);
    }

    public List<DocumentoClinico> listarTodos() {
        return documentoRepository.findAll();
    }
}
