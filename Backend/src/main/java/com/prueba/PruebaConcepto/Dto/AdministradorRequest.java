package com.prueba.PruebaConcepto.Dto;

import com.prueba.PruebaConcepto.entity.Administrador;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministradorRequest {
    private String dominioSubdominio;
    private Administrador administrador;
}
