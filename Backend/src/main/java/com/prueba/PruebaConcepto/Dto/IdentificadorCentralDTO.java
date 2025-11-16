package com.prueba.PruebaConcepto.Dto;

import lombok.Getter;
import lombok.Setter;

// SE USA PARA EL ENVIO DE LOS IDENTIFICADORES DE UN USUARIO LOCAL AL REGISTRARLO PARA EL COMPONENTE CENTRAL

@Getter @Setter
public class IdentificadorCentralDTO {
    private String tipo;
    private String valor;
    private String origen;
}
