package com.prueba.PruebaConcepto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prueba.PruebaConcepto.Dto.IdentificadorRequest;
import com.prueba.PruebaConcepto.Dto.UsuarioCentralDTO;
import com.prueba.PruebaConcepto.Dto.UsuarioRequest;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.Dto.UsuarioMapper;
import com.prueba.PruebaConcepto.entity.IdentificadorUsuario;
import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import com.prueba.PruebaConcepto.repository.UsuarioDeSaludRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioDeSaludService {

    private final UsuarioDeSaludRepository usuarioRepository;
    private final ClinicaRepository clinicaRepository;
    private final CentralSyncService centralSyncService;
    private final UsuarioMapper usuarioMapper;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    public UsuarioDeSaludService(
            UsuarioDeSaludRepository usuarioRepository,
            ClinicaRepository clinicaRepository,
            CentralSyncService centralSyncService,
            UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.clinicaRepository = clinicaRepository;
        this.centralSyncService = centralSyncService;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public UsuarioDeSalud crearUsuarioDesdeRequest(Long clinicaId, UsuarioRequest request) {
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada con ID: " + clinicaId));

        UsuarioDeSalud usuario = new UsuarioDeSalud();
        usuario.setNombre(request.getNombres());
        usuario.setApellido(request.getApellidos());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setSexo(request.getSexo());
        usuario.setDireccion(request.getDireccion());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setClinica(clinica);
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        // Identificadores
        if (request.getIdentificadores() != null && !request.getIdentificadores().isEmpty()) {
            List<IdentificadorUsuario> identificadores = new ArrayList<>();
            for (IdentificadorRequest idReq : request.getIdentificadores()) {
                IdentificadorUsuario ident = new IdentificadorUsuario();
                ident.setTipo(idReq.getTipo());
                ident.setValor(idReq.getValor());
                ident.setOrigen(idReq.getOrigen());
                ident.setFechaAlta(LocalDateTime.now());
                ident.setUsuario(usuario);
                identificadores.add(ident);
            }
            usuario.setIdentificadores(identificadores);
        }

        // Guardar
        UsuarioDeSalud nuevo = usuarioRepository.save(usuario);

        // 🔹 Mapear a DTO y enviar al central
        UsuarioCentralDTO dto = usuarioMapper.toCentralDTO(nuevo, String.valueOf(clinica.getId()));



        try {
            System.out.println("\n📤 JSON ENVIADO AL CENTRAL (Usuario):");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
        } catch (Exception e) {
            System.out.println("❌ Error mostrando JSON del usuario: " + e.getMessage());
        }

      //   (por ahora solo logea, si querés enviar realmente, descomentá)
         centralSyncService.enviarUsuarioAlCentral(dto);


        return nuevo;
    }

    public List<UsuarioDeSalud> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<UsuarioDeSalud> listarPorClinica(Long clinicaId) {
        return usuarioRepository.findByClinicaId(clinicaId);
    }
}
