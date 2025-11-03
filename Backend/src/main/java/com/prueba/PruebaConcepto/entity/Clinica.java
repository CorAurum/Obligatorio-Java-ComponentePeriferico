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
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String telefono;
    private String tipoInstitucion;

    private String dominioSubdominio; // ej: "centrovida.enbodi.xyz"
    private String schemaName;        // schema asociado en PostgreSQL

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
