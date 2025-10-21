package com.prueba.PruebaConcepto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String identificador;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String nombre;
    private String apellido;

    public User() {}

    public User(Long id, String identificador, String password, String email, String nombre, String apellido) {
        this.id = id;
        this.identificador = identificador;
        this.password = password;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
