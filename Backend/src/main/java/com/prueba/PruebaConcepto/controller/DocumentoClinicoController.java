package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import com.prueba.PruebaConcepto.service.DocumentoClinicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
//@CrossOrigin(origins = "*")
public class DocumentoClinicoController {

    private final DocumentoClinicoService documentoService;

    public DocumentoClinicoController(DocumentoClinicoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping
    public ResponseEntity<DocumentoClinico> crearDocumento(
            @RequestParam Long idClinica,
            @RequestParam String idUsuario,
            @RequestParam Long idProfesional,
            @RequestBody DocumentoClinico documento) {

        DocumentoClinico nuevo = documentoService.crearDocumento(idClinica, idUsuario, idProfesional, documento);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public ResponseEntity<List<DocumentoClinico>> listarTodos() {
        return ResponseEntity.ok(documentoService.listarTodos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DocumentoClinico>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(documentoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<List<DocumentoClinico>> listarPorProfesional(@PathVariable Long profesionalId) {
        return ResponseEntity.ok(documentoService.listarPorProfesional(profesionalId));
    }

    @GetMapping("/{id}/externo")
    public ResponseEntity<DocumentoClinico> obtenerDocumentoCompleto(@PathVariable Long id) {
        DocumentoClinico doc = documentoService.listarPorId(id);
        return ResponseEntity.ok(doc);
    }

}
