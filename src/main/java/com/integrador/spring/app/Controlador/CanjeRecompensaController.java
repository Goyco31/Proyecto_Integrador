package com.integrador.spring.app.Controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.spring.app.Modelo.CanjeRecompensa;
import com.integrador.spring.app.Servicio.CanjeRecompensaServices;

@RestController
@RequestMapping("/api/canje")
public class CanjeRecompensaController {

    @Autowired
    private CanjeRecompensaServices services_canjes;

    @GetMapping("")
    public ResponseEntity<List<CanjeRecompensa>> listar() {
        List<CanjeRecompensa> todas = services_canjes.listarTodas();
        return new ResponseEntity<>(todas, HttpStatus.OK);
    }

    @PostMapping("/usuario/{idUser}/recompensa/{idRecompensa}")
    public ResponseEntity<String> canjearRecompensa(@PathVariable Integer idUser, @PathVariable Integer idRecompensa) {
        return services_canjes.canjear(idUser, idRecompensa);
    }
}