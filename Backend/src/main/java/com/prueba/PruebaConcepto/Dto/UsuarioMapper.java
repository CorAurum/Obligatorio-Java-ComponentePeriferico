package com.prueba.PruebaConcepto.Dto;

import com.prueba.PruebaConcepto.entity.IdentificadorUsuario;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public UsuarioCentralDTO toCentralDTO(UsuarioDeSalud usuario, String centroId) {
        UsuarioCentralDTO dto = new UsuarioCentralDTO();

        dto.setIdLocal(usuario.getId() != null ? usuario.getId().toString() : null);
        dto.setCentroId(centroId);
        dto.setNombres(usuario.getNombre());
        dto.setApellidos(usuario.getApellido());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setSexo(usuario.getSexo());
        dto.setDireccion(usuario.getDireccion());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());

        if (usuario.getIdentificadores() != null) {
            List<IdentificadorCentralDTO> identificadoresDTO = usuario.getIdentificadores().stream()
                    .map(this::mapIdentificador)
                    .collect(Collectors.toList());
            dto.setIdentificadores(identificadoresDTO);
        }

        return dto;
    }

    private IdentificadorCentralDTO mapIdentificador(IdentificadorUsuario identificador) {
        IdentificadorCentralDTO dto = new IdentificadorCentralDTO();
        dto.setTipo(identificador.getTipo());
        dto.setValor(identificador.getValor());
        dto.setOrigen(identificador.getOrigen());
        return dto;
    }
}
