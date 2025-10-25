package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "diagnosticos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private LocalDate fechaInicio;
    private String estado;
    private String gradoCerteza;

    @ManyToOne
    @JoinColumn(name = "id_documento")
    private DocumentoClinico documento;
}
