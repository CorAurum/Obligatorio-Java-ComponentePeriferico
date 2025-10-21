package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios_salud")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioDeSalud {

    @Id
    private String cedulaIdentidad;

    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String email;
    private String telefono;
    private LocalDate fechaRegistro;
}
