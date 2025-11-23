package com.prueba.PruebaConcepto.controller;


import com.prueba.PruebaConcepto.entity.Personalizacion;
import com.prueba.PruebaConcepto.service.PersonalizacionService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personalizacion")
public class PersonalizacionController {

    private final PersonalizacionService personalizacionService;

    public PersonalizacionController(PersonalizacionService personalizacionService) {
        this.personalizacionService = personalizacionService;
    }

    // ⭐ POST: Crear configuración
    @PostMapping("/{clinicaId}")
    public Personalizacion crear(@PathVariable String clinicaId,
                                 @RequestBody Personalizacion p) {
        return personalizacionService.crear(p, clinicaId);
    }

    // ⭐ GET: Obtener configuración
    @GetMapping("/{clinicaId}")
    public Personalizacion obtener(@PathVariable String clinicaId) {
        return personalizacionService.obtenerPorClinica(clinicaId);
    }

    // ⭐ PATCH: Modificar config (color, lema, logo)
    @PatchMapping("/{id}")
    public Personalizacion actualizar(@PathVariable Long id,
                                      @RequestBody Personalizacion cambios) {
        return personalizacionService.actualizar(id, cambios);
    }
}

