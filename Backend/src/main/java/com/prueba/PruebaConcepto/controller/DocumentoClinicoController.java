package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.DocumentoClinicoRequest;
import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import com.prueba.PruebaConcepto.service.DocumentoClinicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoClinicoController {

    private final DocumentoClinicoService documentoService;

    public DocumentoClinicoController(DocumentoClinicoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping
    public ResponseEntity<DocumentoClinico> crearDocumento(@RequestBody DocumentoClinicoRequest request) {
        DocumentoClinico nuevo = documentoService.crearDocumento(request.getIdUsuario(), request.getIdProfesional(),
                request.getDocumento(), request.getTenantId());
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public ResponseEntity<List<DocumentoClinico>> listarTodos(@RequestParam String tenantId) {
        return ResponseEntity.ok(documentoService.listarPorClinica(tenantId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DocumentoClinico>> listarPorUsuario(@PathVariable Long usuarioId,
            @RequestParam String tenantId) {
        return ResponseEntity.ok(documentoService.listarPorUsuarioYClinica(usuarioId, tenantId));
    }

    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<List<DocumentoClinico>> listarPorProfesional(@PathVariable String profesionalId,
            @RequestParam String tenantId) {
        return ResponseEntity.ok(documentoService.listarPorProfesionalYClinica(profesionalId, tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoClinico> obtenerPorId(@PathVariable Long id, @RequestParam String tenantId) {
        DocumentoClinico doc = documentoService.listarPorIdYClinica(id, tenantId);
        return ResponseEntity.ok(doc);
    }
}
