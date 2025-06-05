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

import com.integrador.spring.app.Modelo.Equipo;
import com.integrador.spring.app.Servicio.EquipoServices;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private EquipoServices services_equipo;

    @GetMapping("")
    public ResponseEntity<List<Equipo>> listarEquipos() {
        List<Equipo> equipos = services_equipo.listarEquipos();
        return new ResponseEntity<>(equipos, HttpStatus.OK);
    }

    // buscar equipo por su id
    @GetMapping("/id/{id}")
    public ResponseEntity<Equipo> buscarEquipoId(@PathVariable Integer id) {
        Optional<Equipo> equipo = services_equipo.buscarId(id);
        return equipo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // buscar equipo por su nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Equipo> buscarEquipoNombre(@PathVariable String nombre) {
        Optional<Equipo> equipo = services_equipo.buscarNombreEquipo(nombre);
        return equipo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Equipo> registrarEquipo(@RequestBody Equipo equipo) {
        Equipo registrar = services_equipo.guardar(equipo);
        return new ResponseEntity<>(registrar, HttpStatus.CREATED);
    }

    @PutMapping("actualizar/id/{id}")
    public ResponseEntity<Equipo> actualizarEquipoId(@PathVariable Integer id, @RequestBody Equipo equipo) {
        Optional<Equipo> existe = services_equipo.buscarId(id);

        if (existe.isPresent()) {
            Equipo actualizar = existe.get();
            if (equipo.getNombreEquipo() != null) {
                actualizar.setNombreEquipo(equipo.getNombreEquipo());
            }
            return new ResponseEntity<>(services_equipo.guardar(actualizar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("actualizar/nombre/{nombre}")
    public ResponseEntity<Equipo> actualizarEquipoNombre(@PathVariable String nombre, @RequestBody Equipo equipo) {
        Optional<Equipo> existe = services_equipo.buscarNombreEquipo(nombre);

        if (existe.isPresent()) {
            Equipo actualizar = existe.get();
            if (equipo.getNombreEquipo() != null) {
                actualizar.setNombreEquipo(equipo.getNombreEquipo());
            }
            return new ResponseEntity<>(services_equipo.guardar(actualizar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> eliminarEquipoId(@PathVariable Integer id){
        try {
            services_equipo.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/nombre/{nombre}")
    public ResponseEntity<Void> eliminarEquipoNombre(@PathVariable String nombre){
        try {
            services_equipo.eliminarNombre(nombre);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}