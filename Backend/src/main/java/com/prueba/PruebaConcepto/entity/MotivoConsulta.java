package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "motivos_consulta")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MotivoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motivo;

    @ManyToOne
    @JoinColumn(name = "id_documento")
    private DocumentoClinico documento;
}
