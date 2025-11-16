package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grados_certeza")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GradoCerteza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
}
