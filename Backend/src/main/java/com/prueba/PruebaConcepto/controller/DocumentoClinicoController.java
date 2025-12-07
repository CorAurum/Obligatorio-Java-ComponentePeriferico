package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.DocumentoClinicoDTO;
import com.prueba.PruebaConcepto.Dto.DocumentoClinicoParaUsuarioDTO;
import com.prueba.PruebaConcepto.Dto.DocumentoClinicoRequest;
import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import com.prueba.PruebaConcepto.service.ClinicaService;
import com.prueba.PruebaConcepto.service.DocumentoClinicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoClinicoController {

    private final DocumentoClinicoService documentoService;
    private final ClinicaService clinicaService;

    public DocumentoClinicoController(DocumentoClinicoService documentoService, ClinicaService clinicaService) {
        this.documentoService = documentoService;
        this.clinicaService = clinicaService;
    }

    @PostMapping
    public ResponseEntity<DocumentoClinico> crearDocumento(@RequestBody DocumentoClinicoRequest request) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(request.getDominioSubdominio());
        DocumentoClinico nuevo = documentoService.crearDocumento(request.getIdUsuario(), request.getIdProfesional(),
                request.getDocumento(), tenantId);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public ResponseEntity<List<DocumentoClinico>> listarTodos(@RequestParam String dominioSubdominio) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        return ResponseEntity.ok(documentoService.listarPorClinica(tenantId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DocumentoClinico>> listarPorUsuario(@PathVariable Long usuarioId,
            @RequestParam String dominioSubdominio) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        return ResponseEntity.ok(documentoService.listarPorUsuarioYClinica(usuarioId, tenantId));
    }

    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<List<DocumentoClinico>> listarPorProfesional(@PathVariable String profesionalId,
            @RequestParam String dominioSubdominio) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        return ResponseEntity.ok(documentoService.listarPorProfesionalYClinica(profesionalId, tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoClinico> obtenerPorId(@PathVariable String id,
            @RequestParam String dominioSubdominio) {
        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        DocumentoClinico doc = documentoService.listarPorIdYClinica(id, tenantId);
        return ResponseEntity.ok(doc);
    }

    // ESTE GET DEVUELVE AL CENTRAL UN DTO CON EL DOCUMENTO CLINICO ENTERO.
    @GetMapping("/{id}/detalle")
    public ResponseEntity<DocumentoClinicoParaUsuarioDTO> obtenerDetalleParaCentral(
            @PathVariable String id) {

        // Buscar documento por id (y opcionalmente por tenant controlar
        // multi-tenant)
        DocumentoClinico doc = documentoService.listarPorId(id /* opción: tenantId */);
        if (doc == null) {
            return ResponseEntity.status(404).build();
        }

        // Mapear a DTO legible
        DocumentoClinicoParaUsuarioDTO dto = documentoService.mapDocumentoAParaUsuarioDTO(doc);

        return ResponseEntity.ok(dto);
    }

    // GET
    // /api/documentos/usuario/777/dto?profesionalId=PROF123&dominioSubdominio=suat
    // ESTE ENDPOINT ES PARA QUE EL PROFESIONAL DE LA CLINICA PIDA LOS DOCUMENTOS
    // CLINICOS ALOJADOS LOCALMENTE EN LA CLINICA SIN PASAR POR EL COMPONENTE
    // CENTRAL
    @GetMapping("/usuario/{usuarioId}/dto")
    public ResponseEntity<List<DocumentoClinicoDTO>> listarPorUsuarioDTO(
            @PathVariable Long usuarioId,
            @RequestParam String profesionalId,
            @RequestParam String dominioSubdominio) {

        String tenantId = clinicaService.resolverTenantIdPorDominio(dominioSubdominio);
        List<DocumentoClinicoDTO> dtos = documentoService.listarPorUsuarioDTO(usuarioId, profesionalId, tenantId);

        return ResponseEntity.ok(dtos);
    }

}
