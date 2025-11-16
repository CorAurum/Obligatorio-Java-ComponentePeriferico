package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "motivos_consulta")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MotivoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motivo;

    @ManyToMany(mappedBy = "motivosConsulta")
    private List<DocumentoClinico> documentos;
}