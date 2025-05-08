package com.integrador.spring.app.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HolaControlador {
    @GetMapping("skill")
        public String inicio() {
            return "paginaPrincipal"; // Renderiza paginaPrincipal.html
    }
    
    @GetMapping("/Explorar")
        public String explorar() {
            return "Explorar"; // Renderiza Explorar.html
    }
    
    @GetMapping("/Clasificacion")
        public String clasificacion() {
            return "Clasificacion"; // Renderiza Clasificacion.html
    }
    
    @GetMapping("/usuario")
        public String usuario() {
            return "usuario"; // Renderiza usuario.html
    }
 }
