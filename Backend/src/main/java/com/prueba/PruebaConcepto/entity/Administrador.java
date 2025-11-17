package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.*;

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
    // Agregamos atributo cedula para maniobrar en el front - experimental
    private String cedula;
    private String email;
    private String usuario;
    private String CreadorPor;
    private String contrasena; // almacenada con hash+salt
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "clinica_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Clinica clinica;
}
