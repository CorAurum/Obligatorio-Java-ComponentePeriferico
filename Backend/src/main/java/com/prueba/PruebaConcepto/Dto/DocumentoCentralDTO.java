package com.prueba.PruebaConcepto.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// SE USA PARA EL ENVIO DEL DOCUMENTO AL REGISTRARLO PARA EL COMPONENTE CENTRAL



@Getter @Setter
public class DocumentoCentralDTO {
    private String idOrigen;
    private String centroId;
    private String profesionalId;
    private String usuarioLocalId;
    private String titulo;
    private String descripcion;
    private String tipoDocumento;
    private String area;
    private LocalDateTime fechaCreacion;
    private String urlAlojamiento;
}
