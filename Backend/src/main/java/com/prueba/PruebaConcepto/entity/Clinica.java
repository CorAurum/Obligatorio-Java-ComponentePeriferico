package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo( // 👈 evita bucles mostrando solo IDs en las referencias
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String telefono;
    private String tipoInstitucion;

    private String dominioSubdominio; // ej: "centrovida.enbodi.xyz"

    private LocalDateTime fechaAlta;
    private LocalDateTime fechaBaja;

    // Relaciones
    @OneToMany(mappedBy = "clinica")
    private List<Administrador> administradores;

    @OneToMany(mappedBy = "clinica")
    private List<ProfesionalDeSalud> profesionales;

    @OneToMany(mappedBy = "clinica")
    private List<UsuarioDeSalud> usuariosDeSalud;
}
