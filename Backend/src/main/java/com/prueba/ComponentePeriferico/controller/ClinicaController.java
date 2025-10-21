package com.prueba.PruebaConcepto.controller;

import java.util.List;
import com.prueba.PruebaConcepto.Dto.ClinicaDto;
import com.prueba.PruebaConcepto.service.ClinicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clinicas")
@CrossOrigin(origins = "http://localhost:5173")
public class ClinicaController {

    private final ClinicaService clinicaService;

    public ClinicaController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    @PostMapping("/add")
    public ResponseEntity<ClinicaDto> addClinica(@RequestBody ClinicaDto clinicaDto) {
        return ResponseEntity.ok(clinicaService.crearClinica(clinicaDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClinicaDto>> listarClinicas() {
        return ResponseEntity.ok(clinicaService.listarClinicas());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClinicaDto> updateClinica(
            @PathVariable Long id,
            @RequestBody ClinicaDto clinicaDto
    ) {
        return ResponseEntity.ok(clinicaService.modificarClinica(id, clinicaDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteClinica(@PathVariable Long id) {
        clinicaService.eliminarClinica(id);
        return ResponseEntity.ok("Clinica eliminada con éxito");
    }
}
