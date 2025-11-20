package com.prueba.PruebaConcepto.Dto;

import java.time.LocalDateTime;

public class DocumentoClinicoDTO {

    private LocalDateTime fechaCreacion;
    private String area;
    private String descripcion;
    private String documentoId;
    private String profesionalNombre;
    private String profesionalApellido;

    public DocumentoClinicoDTO(LocalDateTime fechaCreacion, String area, String descripcion,
                               String documentoId, String profesionalNombre, String profesionalApellido) {
        this.fechaCreacion = fechaCreacion;
        this.area = area;
        this.descripcion = descripcion;
        this.documentoId = documentoId;
        this.profesionalNombre = profesionalNombre;
        this.profesionalApellido = profesionalApellido;
    }

    // getters y setters
}