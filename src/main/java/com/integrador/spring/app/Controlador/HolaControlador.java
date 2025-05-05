package com.integrador.spring.app.Controlador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaControlador {
    @GetMapping("/")
    public String inicio(){
        return"index";
    }
}
