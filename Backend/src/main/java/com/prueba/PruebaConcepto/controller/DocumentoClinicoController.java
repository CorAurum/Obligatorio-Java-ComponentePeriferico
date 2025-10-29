package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.DocumentoClinicoDto;
import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import com.prueba.PruebaConcepto.service.DocumentoClinicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documentos")
@CrossOrigin(origins = "http://localhost:5173")
public class DocumentoClinicoController {

    private final DocumentoClinicoService documentoService;

    public DocumentoClinicoController(DocumentoClinicoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping("/add")
    public ResponseEntity<DocumentoClinicoDto> add(@RequestBody DocumentoClinicoDto dto) {
        return ResponseEntity.ok(documentoService.crearDocumento(dto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        documentoService.eliminarDocumento(id);
        return ResponseEntity.ok("Documento eliminado con éxito");
    }

    @GetMapping("/list")
    public ResponseEntity<List<DocumentoClinicoDto>> listAll() {
        return ResponseEntity.ok(documentoService.listarTodos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<DocumentoClinico> getDocumentoCompleto(@PathVariable Long id) {
        DocumentoClinico documento = documentoService.obtenerPorId(id);
        if (documento == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(documento);
    }

}
