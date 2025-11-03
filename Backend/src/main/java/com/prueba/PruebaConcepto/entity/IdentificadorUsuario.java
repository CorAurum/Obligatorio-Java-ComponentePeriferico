package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdentificadorUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String valor;
    private String origen;
    private LocalDateTime fechaAlta;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioDeSalud usuario;

    // Getters y setters
}
