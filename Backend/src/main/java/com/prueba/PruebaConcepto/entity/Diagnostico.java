package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnosticos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private LocalDateTime fechaInicio;

    @ManyToOne
    @JoinColumn(name = "grado_certeza_id")
    private GradoCerteza gradoCerteza;

    @ManyToOne
    @JoinColumn(name = "estado_problema_id")
    private EstadoProblema estadoProblema;

    @ManyToOne
    @JoinColumn(name = "documento_id")
    private DocumentoClinico documentoClinico;
}

