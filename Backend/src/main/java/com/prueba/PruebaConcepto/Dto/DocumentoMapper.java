package com.prueba.PruebaConcepto.Dto;

import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DocumentoMapper {

    public DocumentoCentralDTO toCentralDTO(DocumentoClinico doc, String centroId) {
        DocumentoCentralDTO dto = new DocumentoCentralDTO();

        dto.setIdOrigen(doc.getId() != null ? doc.getId().toString() : null);
        dto.setCentroId(centroId);
        dto.setTitulo(doc.getTitulo());
        dto.setDescripcion(doc.getDescripcion());
        dto.setTipoDocumento(doc.getTipoDocumento());
        dto.setArea(doc.getArea());
        dto.setFechaCreacion(doc.getFechaCreacion());
        dto.setUrlAlojamiento(doc.getUrlAlojamiento() != null ?
                doc.getUrlAlojamiento() :
                "https://placeholder.docs/" + doc.getId());

        // Asociar relaciones internas (si existen)
        if (doc.getProfesional() != null) {
            dto.setProfesionalId(String.valueOf(doc.getProfesional().getIdProfesional()));
        }
        if (doc.getUsuario() != null) {
            dto.setUsuarioLocalId(String.valueOf(doc.getUsuario().getId()));
        }

        return dto;
    }
}
