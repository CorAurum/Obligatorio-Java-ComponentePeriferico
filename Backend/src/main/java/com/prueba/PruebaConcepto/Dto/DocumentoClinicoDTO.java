package com.prueba.PruebaConcepto.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DocumentoClinicoDTO {
    private LocalDateTime fechaCreacion;
    private String area;
    private String descripcion;
    private String documentoId;
    private String profesionalNombre;
    private String profesionalApellido;
}
