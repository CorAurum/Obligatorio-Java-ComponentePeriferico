package com.prueba.PruebaConcepto.service;


import com.prueba.PruebaConcepto.entity.Clinica;
import com.prueba.PruebaConcepto.entity.Personalizacion;
import com.prueba.PruebaConcepto.repository.ClinicaRepository;
import com.prueba.PruebaConcepto.repository.PersonalizacionRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonalizacionService {

    private final PersonalizacionRepository personalizacionRepository;
    private final ClinicaRepository clinicaRepository;

    public PersonalizacionService(PersonalizacionRepository personalizacionRepository,
                                  ClinicaRepository clinicaRepository) {
        this.personalizacionRepository = personalizacionRepository;
        this.clinicaRepository = clinicaRepository;
    }

    // Crear registro
    public Personalizacion crear(Personalizacion p, String clinicaId) {

        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new RuntimeException("Clínica no encontrada"));

        p.setClinica(clinica);
        return personalizacionRepository.save(p);
    }

    // Obtener por clínica
    public Personalizacion obtenerPorClinica(String clinicaId) {
        return personalizacionRepository.findByClinica_Id(clinicaId)
                .orElseThrow(() -> new RuntimeException("No hay personalización registrada"));
    }

    // Modificar solo color, lema y logo
    public Personalizacion actualizar(Long id, Personalizacion cambios) {
        Personalizacion actual = personalizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personalización no encontrada"));

        if (cambios.getColor() != null)
            actual.setColor(cambios.getColor());

        if (cambios.getLema() != null)
            actual.setLema(cambios.getLema());

        if (cambios.getLogo() != null)
            actual.setLogo(cambios.getLogo());

        // NO se modifica la clínica ni la PK

        return personalizacionRepository.save(actual);
    }
}
