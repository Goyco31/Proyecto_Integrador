package com.integrador.spring.app.Controlador;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ControladorVistas {
    @GetMapping("/")
    public String inicio() {
        return "paginaPrincipal";
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
        return "noticias";
    } 

    @GetMapping("/administrador")
    @PreAuthorize("hasRole('ADMIN')")
    public String vistaAdmin() {
        return "administrador"; // nombre de tu template
    }  
}
