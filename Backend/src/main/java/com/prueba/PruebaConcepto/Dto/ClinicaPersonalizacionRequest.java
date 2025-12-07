package com.prueba.PruebaConcepto.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClinicaPersonalizacionRequest {
    private String nombrePublico;
    private String eslogan;
    private String colorPrimario;
    private String colorSecundario;
    private String logoUrl;
}
