package com.prueba.PruebaConcepto.Dto;

import com.prueba.PruebaConcepto.entity.DocumentoClinico;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentoClinicoRequest {
    private String tenantId;
    private String idUsuario;
    private String idProfesional;
    private DocumentoClinico documento;
}
