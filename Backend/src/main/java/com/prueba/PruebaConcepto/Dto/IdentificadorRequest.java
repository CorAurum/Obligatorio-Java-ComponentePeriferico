package com.prueba.PruebaConcepto.Dto;

// la clase se usa para crear un usuario de la clinica con sus identificadores correspondientes

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IdentificadorRequest {
    private String tipo;
    private String valor;
    private String origen;
}
