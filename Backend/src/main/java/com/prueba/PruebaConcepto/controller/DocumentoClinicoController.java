package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.DocumentoClinicoParaUsuarioDTO;
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
    public ResponseEntity<DocumentoClinico> obtenerPorId(@PathVariable String id, @RequestParam String tenantId) {
        DocumentoClinico doc = documentoService.listarPorIdYClinica(id, tenantId);
        return ResponseEntity.ok(doc);
    }

    // ESTE GET DEVUELVE AL CENTRAL UN DTO CON EL DOCUMENTO CLINICO ENTERO


    @GetMapping("/{id}/detalle")
    public ResponseEntity<DocumentoClinicoParaUsuarioDTO> obtenerDetalleParaCentral(
            @PathVariable String id) {

        // Buscar documento por id (y opcionalmente por tenant si querés controlar multi-tenant)
        DocumentoClinico doc = documentoService.listarPorId(id /* opción: tenantId si querés */);
        if (doc == null) {
            return ResponseEntity.status(404).build();
        }

        // Mapear a DTO legible
        DocumentoClinicoParaUsuarioDTO dto = documentoService.mapDocumentoAParaUsuarioDTO(doc);

        return ResponseEntity.ok(dto);
    }


}
