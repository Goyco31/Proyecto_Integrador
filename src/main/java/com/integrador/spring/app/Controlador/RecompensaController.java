package com.integrador.spring.app.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecompensaController {

    @GetMapping("/index")
    public String mostrarRecompensas(Model model) {
        // Se pueden enviar datos simples como título o nombre del sitio
        model.addAttribute("pageTitle", "SkillTourney | Premios para Canjear");
        model.addAttribute("siteName", "SkillTourney");

        return "canjes";
}
}