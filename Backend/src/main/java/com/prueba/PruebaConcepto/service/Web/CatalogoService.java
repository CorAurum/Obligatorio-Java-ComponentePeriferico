package com.prueba.PruebaConcepto.service.Web;

import com.prueba.PruebaConcepto.entity.EstadosProblemas;
import com.prueba.PruebaConcepto.entity.GradosCerteza;
import com.prueba.PruebaConcepto.entity.Motivos;
import com.prueba.PruebaConcepto.repository.EstadosProblemasRepository;
import com.prueba.PruebaConcepto.repository.GradosCertezaRepository;
import com.prueba.PruebaConcepto.repository.MotivosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoService {

    private final MotivosRepository motivosRepo;
    private final GradosCertezaRepository gradosRepo;
    private final EstadosProblemasRepository estadosRepo;

    public CatalogoService(MotivosRepository motivosRepo,
                           GradosCertezaRepository gradosRepo,
                           EstadosProblemasRepository estadosRepo) {
        this.motivosRepo = motivosRepo;
        this.gradosRepo = gradosRepo;
        this.estadosRepo = estadosRepo;
    }

    public List<Motivos> listarMotivos() {
        return motivosRepo.findAll();
    }

    public List<GradosCerteza> listarGradosCerteza() {
        return gradosRepo.findAll();
    }

    public List<EstadosProblemas> listarEstados() {
        return estadosRepo.findAll();
    }
}
