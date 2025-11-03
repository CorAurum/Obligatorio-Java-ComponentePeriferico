package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documentos_clinicos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DocumentoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private String areaProximoControl;

    private LocalDateTime fechaProximaConsultaRecomendada;
    private LocalDateTime fechaProximaConsultaConfirmada;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioDeSalud usuario;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private ProfesionalDeSalud profesional;

    @ManyToMany
    @JoinTable(
            name = "documento_motivos",
            joinColumns = @JoinColumn(name = "documento_id"),
            inverseJoinColumns = @JoinColumn(name = "motivo_id")
    )
    private List<MotivoConsulta> motivosConsulta;

    @OneToMany(mappedBy = "documentoClinico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnostico> diagnosticos;
}
