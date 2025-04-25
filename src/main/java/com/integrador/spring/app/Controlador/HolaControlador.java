package com.integrador.spring.app.Controlador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaControlador {
    @GetMapping("/")
    String hola(){
        return"Hola Mundo con Darkcode";
    }
}
