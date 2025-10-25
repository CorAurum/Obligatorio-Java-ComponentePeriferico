package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gradosCerteza")
@Getter @Setter
public class GradosCerteza {

    @Id
    private Long id;
    private String gradoCerteza;

}