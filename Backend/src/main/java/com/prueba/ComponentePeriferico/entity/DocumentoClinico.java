package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documentos_clinicos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DocumentoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumento;

    private LocalDateTime fechaCreacion;

    //Instrucciones de seguimiento
    private LocalDateTime fechaProximaConsultaRecomendada;
    private LocalDateTime fechaProximaConsultaConfirmada;
    private String areaProximoControl;

    @ManyToOne
    @JoinColumn(name = "id_clinica")
    private Clinica clinica;

    @ManyToOne
    @JoinColumn(name = "cedula_usuario", nullable = false)
    private UsuarioDeSalud usuario;

    @ManyToOne
    @JoinColumn(name = "id_profesional", nullable = false)
    private ProfesionalDeSalud profesional;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MotivoConsulta> motivosConsulta;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnostico> diagnosticos;
}
