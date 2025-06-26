package com.integrador.spring.app.Controlador;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.integrador.spring.app.Modelo.ComprarMonedas;
import com.integrador.spring.app.Servicio.ComprarMonedasServices;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/comprarMonedas")
public class CompraMonedasController {

    @Autowired
    private ComprarMonedasServices services_compra;

    @GetMapping("/lista")
    public ResponseEntity<List<ComprarMonedas>> listarOpciones() {
        List<ComprarMonedas> todos = services_compra.listar();

        for (ComprarMonedas monedas : todos) {
            if (monedas.getImgMoneda() != null) {
                byte[] imgMoneda = monedas.getImgMoneda();
                String imgMonedaBase64 = Base64.getEncoder().encodeToString(imgMoneda);
                monedas.setImgimgMonedaBase64(imgMonedaBase64);
            }
        }
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ComprarMonedas> buscarId(@PathVariable Integer id) {
        Optional<ComprarMonedas> existe = services_compra.buscarId(id);
        return existe.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/registrar")
    public ResponseEntity<ComprarMonedas> agregarOpcion(
            @RequestParam("nombre") String nombre,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("precio") BigDecimal precioCompra,
            @RequestParam("imagen") MultipartFile imgMoneda) throws java.io.IOException {

        try {
            ComprarMonedas nueva = services_compra.añadirCompra(nombre, cantidad, precioCompra, imgMoneda);
            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("actualizar/id/{id}")
    public ResponseEntity<ComprarMonedas> actualizarOpciones(
            @PathVariable Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("precio") BigDecimal precioCompra,
            @RequestParam("imagen") MultipartFile imgMoneda) {
        try {
            ComprarMonedas actualizar = services_compra.actualizarCompra(id, nombre, cantidad, precioCompra, imgMoneda);
            return ResponseEntity.ok(actualizar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Void> eliminarOpcion(@PathVariable Integer id) {
        try {
            services_compra.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}