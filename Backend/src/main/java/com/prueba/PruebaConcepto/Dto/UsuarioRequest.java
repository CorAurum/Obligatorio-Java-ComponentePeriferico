package com.prueba.PruebaConcepto.Dto;

// la clase se usa para crear un usuario de la clinica con sus identificadores correspondientes

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class UsuarioRequest {
    // Si usás path variable para clinica, no hace falta enviar centroId aquí.
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String direccion;
    private String email;
    private String telefono;
    private List<IdentificadorRequest> identificadores;
}

