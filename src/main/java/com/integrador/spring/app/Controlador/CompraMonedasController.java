package com.integrador.spring.app.Controlador;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.spring.app.Modelo.ComprarMonedas;
import com.integrador.spring.app.Servicio.ComprarMonedasServices;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/comprarMonedas")
public class CompraMonedasController {

    @Autowired
    private ComprarMonedasServices services_compra;

    @GetMapping("")
    public ResponseEntity<List<ComprarMonedas>> listarOpciones() {
        List<ComprarMonedas> todos = services_compra.listar();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PostMapping("/registar")
    public ResponseEntity<ComprarMonedas> agregarOpcion(@RequestBody ComprarMonedas comprarMonedas) {
        ComprarMonedas registrar = services_compra.guardar(comprarMonedas);
        return new ResponseEntity<>(registrar, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/id/{id}")
    public ResponseEntity<ComprarMonedas> actualizarOpciones(@PathVariable Integer id,
            @RequestBody ComprarMonedas comprarMonedas) {
        Optional<ComprarMonedas> existe = services_compra.buscarId(id);
        if (existe.isPresent()) {
            ComprarMonedas actualizar = existe.get();
            if (comprarMonedas.getNombre() != null) {
                actualizar.setNombre(comprarMonedas.getNombre());
            }
            if (comprarMonedas.getCantidad() != null) {
                actualizar.setCantidad(comprarMonedas.getCantidad());
            }
            if (comprarMonedas.getPrecioCompra() != null) {
                actualizar.setPrecioCompra(comprarMonedas.getPrecioCompra());
            }
            return new ResponseEntity<>(services_compra.guardar(actualizar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarOpcion(Integer id){
        try {
            services_compra.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}