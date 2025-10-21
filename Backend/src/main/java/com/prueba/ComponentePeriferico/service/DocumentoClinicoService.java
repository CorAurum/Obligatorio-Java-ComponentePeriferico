package com.prueba.PruebaConcepto.service;

import com.prueba.PruebaConcepto.Dto.*;
import com.prueba.PruebaConcepto.entity.*;
import com.prueba.PruebaConcepto.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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





}
