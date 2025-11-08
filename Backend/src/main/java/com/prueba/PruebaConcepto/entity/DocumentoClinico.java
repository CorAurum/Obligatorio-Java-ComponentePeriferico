package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documentos_clinicos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Filter(name = "tenantFilter", condition = "clinica_id = :clinicaId")
public class DocumentoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private String areaProximoControl;

    private String titulo;
    private String descripcion;
    private String TipoDocumento;
    private String FechaCreacion;
    private String UrlAlojamiento;

    private LocalDateTime fechaProximaConsultaRecomendada;
    private LocalDateTime fechaProximaConsultaConfirmada;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIdentityReference(alwaysAsId = true)
    private UsuarioDeSalud usuario;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private ProfesionalDeSalud profesional;

    @ManyToOne
    @JoinColumn(name = "clinica_id")
    private Clinica clinica;

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
