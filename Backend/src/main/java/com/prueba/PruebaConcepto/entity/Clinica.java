package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clinicas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoClinica;
    private String nombre;
    private String direccion;
    private String telefono;
    private String tipoInstitucion;
}
