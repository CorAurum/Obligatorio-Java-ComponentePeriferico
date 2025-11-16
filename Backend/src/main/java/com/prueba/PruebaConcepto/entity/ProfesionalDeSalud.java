package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionalDeSalud {

    @Id
    private String idProfesional;

    private String cedulaIdentidad;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "clinica_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Clinica clinica;

    @ManyToMany
    @JoinTable(name = "profesional_especialidad", joinColumns = @JoinColumn(name = "profesional_id"), inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    private List<Especialidad> especialidades;

    @OneToMany(mappedBy = "profesional")
    private List<DocumentoClinico> documentos;
}
