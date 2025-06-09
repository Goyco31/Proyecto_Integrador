package com.integrador.spring.app.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ControladorVistas {
    @GetMapping("/")
    public String inicio() {
        return "paginaPrincipal";
    }

    @GetMapping("/index")
    public String mostrarRegistro() {
        return "index";
    }

    @GetMapping("/paginaTorneo")
    public String paginatorneo() {
        return "paginaTorneo";
    }

    @GetMapping("/explorar")
    public String explorar() {
        return "Explorar";
    }
    
    @GetMapping("/clasificacion")
    public String clasificacion() {
        return "Clasificacion";
    }
    
    @GetMapping("/usuario")
    public String usuario() {
        return "usuario";
    }
    
    @GetMapping("/noticias")
    public String noticias(){
        return "/noticias";
    }    

    @GetMapping("/canjes")
    public String mostrarRecompensas(Model model) {
        // Se pueden enviar datos simples como título o nombre del sitio
        model.addAttribute("pageTitle", "SkillTourney | Premios para Canjear");
        model.addAttribute("siteName", "SkillTourney");

        return "canjes";
    }
}
