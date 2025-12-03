package com.prueba.PruebaConcepto.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClinicaBajaDTO {
    private String id;
    private LocalDateTime fechaBaja;

    // getters y setters
}
