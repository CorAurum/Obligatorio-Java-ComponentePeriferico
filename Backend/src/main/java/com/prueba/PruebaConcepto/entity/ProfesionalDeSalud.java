package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profesionales_salud")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProfesionalDeSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfesional;

    private String cedulaIdentidad;
    private String nombre;
    private String apellido;
    private String numeroRegistro;
    private String email;
    private String telefono;
}
