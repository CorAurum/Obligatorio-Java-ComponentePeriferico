package com.prueba.PruebaConcepto.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProfesional")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionalDeSalud implements UserDetails {

    @Id
    private String idProfesional;

    private String cedulaIdentidad;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private boolean activo = true;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.PROFESIONAL;

    @ManyToOne
    @JoinColumn(name = "clinica_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Clinica clinica;

    @ManyToMany
    @JoinTable(name = "profesional_especialidad", joinColumns = @JoinColumn(name = "profesional_id"), inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    private List<Especialidad> especialidades;

    @OneToMany(mappedBy = "profesional")
    @JsonIdentityReference(alwaysAsId = true)
    private List<DocumentoClinico> documentos;

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.cedulaIdentidad;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.activo;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.activo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.activo;
    }
}
