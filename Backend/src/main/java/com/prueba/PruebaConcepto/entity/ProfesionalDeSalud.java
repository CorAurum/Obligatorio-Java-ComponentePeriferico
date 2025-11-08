package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Filter(name = "tenantFilter", condition = "clinica_id = :clinicaId")
public class ProfesionalDeSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfesional;

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
    @JoinTable(
            name = "profesional_especialidad",
            joinColumns = @JoinColumn(name = "profesional_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    private List<Especialidad> especialidades;

    @OneToMany(mappedBy = "profesional")
    private List<DocumentoClinico> documentos;
}

