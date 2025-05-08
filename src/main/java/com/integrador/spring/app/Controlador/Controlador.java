package com.integrador.spring.app.Controlador;


// Importaciones necesarias para manejar respuestas HTTP y anotaciones de Spring
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// Anotación que indica que esta clase es un controlador REST de Spring
@Controller
// Define la ruta base para todas las solicitudes que maneja este controlador
@RequestMapping("/control")
// Genera un constructor con los atributos requeridos (final), útil para inyección de dependencias
@RequiredArgsConstructor
public class Controlador {
    // Servicio que maneja la lógica de autenticación y registro
    private final ControladorService authService;
    
    //para el manejo de solicitud post a la ruta: "/""
    @GetMapping("/")
    public String mostrarRegistro() {
        return "index";
    }


    // Recibe los datos de login en el cuerpo de la solicitud y los pasa al servicio
    @PostMapping(value = "login")
    @ResponseBody
    public ResponseEntity<ControladorResponse> login(@RequestBody LoginRequest request) {
        // Devuelve la respuesta del servicio envuelta en un ResponseEntity con estado HTTP 200 OK
        return ResponseEntity.ok(authService.login(request));
    }

    // Recibe los datos de registro en el cuerpo de la solicitud y los pasa al servicio(para el manejo de solicitud post a la ruta: "/control/registro")
    @PostMapping(value = "registro")
    @ResponseBody
    public ResponseEntity<ControladorResponse> registro(@RequestBody RegisterRequest request) {
        // Devuelve la respuesta del servicio envuelta en un ResponseEntity con estado HTTP 200 OK
        return ResponseEntity.ok(authService.registro(request));
    }
  
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