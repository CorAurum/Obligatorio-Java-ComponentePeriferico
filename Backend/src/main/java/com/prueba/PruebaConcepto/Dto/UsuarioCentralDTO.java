package com.prueba.PruebaConcepto.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// SE USA PARA EL ENVIO DEL USUARIO AL REGISTRARLO PARA EL COMPONENTE CENTRAL

@Getter @Setter
public class UsuarioCentralDTO {
    private String idLocal;
    private String centroId;
    private String nombres;
    private String apellidos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String sexo;
    private String direccion;
    private String email;
    private String telefono;
    private List<IdentificadorCentralDTO> identificadores;
}

