package com.integrador.spring.app.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HolaControlador {
    @GetMapping("/")
    public String inicio(){
        return"index";
    }
}
