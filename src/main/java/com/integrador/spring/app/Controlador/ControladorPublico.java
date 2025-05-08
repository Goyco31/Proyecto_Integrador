package com.integrador.spring.app.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorPublico {
    @GetMapping("/")
    public String mostrarRegistro() {
        return "index";
    }

    @GetMapping("/Explorar")
    public String explorar() {
        return "Explorar";
    }

    @GetMapping("/Clasificacion")
    public String clasificacion() {
        return "Clasificacion";
    }

    @GetMapping("/usuario")
    public String usuario() {
        return "usuario";
    }

    @GetMapping("/skill")
    public String inicio() {
        return "paginaPrincipal";
    }
}
