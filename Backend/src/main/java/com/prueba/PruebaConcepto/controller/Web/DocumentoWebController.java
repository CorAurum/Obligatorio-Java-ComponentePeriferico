package com.prueba.PruebaConcepto.controller.Web;

import com.prueba.PruebaConcepto.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentoWebController {

    private final UsuarioDeSaludRepository usuarioRepo;
    private final ProfesionalDeSaludRepository profesionalRepo;
    private final EstadosProblemasRepository estadosRepo;
    private final GradosCertezaRepository gradosRepo;

    public DocumentoWebController(
            UsuarioDeSaludRepository usuarioRepo,
            ProfesionalDeSaludRepository profesionalRepo,
            EstadosProblemasRepository estadosRepo,
            GradosCertezaRepository gradosRepo
    ) {
        this.usuarioRepo = usuarioRepo;
        this.profesionalRepo = profesionalRepo;
        this.estadosRepo = estadosRepo;
        this.gradosRepo = gradosRepo;
    }

    @GetMapping("/web/documentos/form")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        model.addAttribute("profesionales", profesionalRepo.findAll());
        model.addAttribute("estados", estadosRepo.findAll());
        model.addAttribute("gradosCerteza", gradosRepo.findAll());
        return "crear_documento";
    }
}
