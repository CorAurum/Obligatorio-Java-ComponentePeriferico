package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documentos_clinicos")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private String areaProximoControl;

    private String titulo;
    private String descripcion;
    private String TipoDocumento;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private String UrlAlojamiento;

    @Column(nullable = true)
    private LocalDateTime fechaProximaConsultaRecomendada;

    @Column(nullable = true)
    private LocalDateTime fechaProximaConsultaConfirmada;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIdentityReference(alwaysAsId = true)
    private UsuarioDeSalud usuario;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    @JsonIdentityReference(alwaysAsId = true)
    private ProfesionalDeSalud profesional;

    @ManyToOne
    @JoinColumn(name = "clinica_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Clinica clinica;

    @ManyToMany
    @JoinTable(name = "documento_motivos", joinColumns = @JoinColumn(name = "documento_id"), inverseJoinColumns = @JoinColumn(name = "motivo_id"))
    private List<MotivoConsulta> motivosConsulta;

    @OneToMany(mappedBy = "documentoClinico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnostico> diagnosticos;

}
