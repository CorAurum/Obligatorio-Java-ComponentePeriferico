package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UsuarioDeSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String direccion;
    private String email;
    private String telefono;
    private LocalDateTime fechaRegistro;
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "clinica_id")
    private Clinica clinica;

    @OneToMany(mappedBy = "usuario")
    private List<DocumentoClinico> documentos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdentificadorUsuario> identificadores;
}
