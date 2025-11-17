package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
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
    @JsonIdentityReference(alwaysAsId = true)
    private Clinica clinica;

    @OneToMany(mappedBy = "usuario")
    @JsonIdentityReference(alwaysAsId = true)
    private List<DocumentoClinico> documentos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdentificadorUsuario> identificadores;
}
