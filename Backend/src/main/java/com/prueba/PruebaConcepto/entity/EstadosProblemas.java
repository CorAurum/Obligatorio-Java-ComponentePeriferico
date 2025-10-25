package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estadosProblemas")
@Getter @Setter
public class EstadosProblemas {

    @Id
    private Long id;
    private String estadoProblema;

}