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

import com.integrador.spring.app.Modelo.Recarga;
import com.integrador.spring.app.Servicio.RecargaServices;

@RestController
@RequestMapping("/api/recarga")
public class RecargaController {
    @Autowired
    private RecargaServices services_recarga;

    @GetMapping("")
    public ResponseEntity<List<Recarga>> listar() {
        List<Recarga> todas = services_recarga.listarRecarga();
        return new ResponseEntity<>(todas, HttpStatus.OK);
    }

    @PostMapping("/usuario/{idUser}/recarga/{idCompra}")
    public ResponseEntity<String> recargarMonedas(@PathVariable Integer idUser, @PathVariable Integer idCompra) {
        return services_recarga.recargar(idUser, idCompra);
    }

}
