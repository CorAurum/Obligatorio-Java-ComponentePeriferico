package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "motivos_consulta")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
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