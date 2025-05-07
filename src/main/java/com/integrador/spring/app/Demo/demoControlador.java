package com.integrador.spring.app.Demo;

// Anotaciones de Spring para definir el comportamiento del controlador
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Anotaciones de Lombok para evitar escribir código repetitivo
import lombok.RequiredArgsConstructor;

// Importación para manejar solicitudes HTTP POST
import org.springframework.web.bind.annotation.PostMapping;


// Marca esta clase como un controlador REST
@RestController
// Define el path base para todas las rutas de este controlador (por ejemplo: /api/v1/demo)
@RequestMapping("/api/v1")

@RequiredArgsConstructor
public class demoControlador {
    // Maneja solicitudes POST al endpoint "/api/v1/demo"
    @PostMapping(value = "demo")
    public String welcome() {
        // Devuelve un mensaje simple como respuesta del endpoint protegido
        return "Welcome from secure endpoint";
    }
    
}
