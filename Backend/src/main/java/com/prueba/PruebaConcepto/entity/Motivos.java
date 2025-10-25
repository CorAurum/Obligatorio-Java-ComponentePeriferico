package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "motivos")
@Getter @Setter
public class Motivos {

    @Id
    private Long id;
    private String motivo;

}