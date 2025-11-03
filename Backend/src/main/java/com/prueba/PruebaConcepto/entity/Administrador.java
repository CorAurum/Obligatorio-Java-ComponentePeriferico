package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String usuario;
    private String CreadorPor;
    private String contrasena; // almacenada con hash+salt
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "clinica_id")
    private Clinica clinica;
}
