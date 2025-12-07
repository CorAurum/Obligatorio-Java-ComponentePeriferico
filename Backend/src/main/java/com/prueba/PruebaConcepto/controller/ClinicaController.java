package com.prueba.PruebaConcepto.controller;

import com.prueba.PruebaConcepto.Dto.ClinicaBajaDTO;
import com.prueba.PruebaConcepto.Dto.ClinicaDTO;
import com.prueba.PruebaConcepto.Dto.ClinicaPersonalizacionRequest;
import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.service.ClinicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinicas")
// @CrossOrigin(origins = "*")
public class ClinicaController {

    private final ClinicaService clinicaService;

    public ClinicaController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    @PostMapping
    public ResponseEntity<Clinica> crearClinica(@RequestBody Clinica clinica) {
        Clinica nueva = clinicaService.crearClinica(clinica);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping
    public ResponseEntity<List<Clinica>> listarClinicas() {
        return ResponseEntity.ok(clinicaService.listarClinicas());
    }

    @GetMapping("/{dominioSubdominio}")
    public ResponseEntity<Clinica> obtenerPorDominio(@PathVariable String dominioSubdominio) {
        return clinicaService.obtenerPorDominio(dominioSubdominio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/averiguar/{CI}")
    public ResponseEntity<String> obtenerPorCI(@PathVariable String CI) {
        return clinicaService.obtenerDominioPorCedula(CI)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dominio/{dominio}/info")
    public ResponseEntity<ClinicaDTO> obtenerClinicaInfo(@PathVariable String dominio) {
        ClinicaDTO dto = clinicaService.obtenerClinicaDTO(dominio);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/personalizacion")
    public ResponseEntity<Clinica> actualizarPersonalizacion(@PathVariable String id,
            @RequestBody ClinicaPersonalizacionRequest dto) {
        Clinica actualizada = clinicaService.actualizarPersonalizacion(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @PostMapping("/baja")
    public ResponseEntity<Clinica> marcarBaja(@RequestBody ClinicaBajaDTO dto) {
        Clinica actualizada = clinicaService.marcarFechaBaja(dto.getId(), dto.getFechaBaja());
        return ResponseEntity.ok(actualizada);
    }

}
