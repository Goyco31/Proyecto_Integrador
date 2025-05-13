package com.integrador.spring.app.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//se encarga de controlar las rutas públicas accesibles en la aplicacíon web 
@Controller
public class ControladorPublico {

    //Ruta que carga la página del inicio 
    @GetMapping("/")
    public String mostrarRegistro() {
        return "index";
    }

    //Ruta para la sección de exploración de contenido 
    @GetMapping("/Explorar")
    public String explorar() {
        return "Explorar";
    }

    //Ruta para mostrar la clasificación o ranking de jugadores/clanes 
    @GetMapping("/Clasificacion")
    public String clasificacion() {
        return "Clasificacion";
    }

    //Ruta para mostrar la tienda 
    @GetMapping("/Tienda")
    public String tienda() {
        return "Tienda";
    }

    //Ruta para la pagina principal del torneo     
    @GetMapping("/skill")
    public String inicio() {
        return "paginaTorneol";
    }

    //Ruta para acceder al perfin del usuario 
     @GetMapping("/usuario")
    public String usuario() {
        return "usuario";
    }
}
