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

import com.integrador.spring.app.Modelo.Juego;
import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Servicio.JuegoServices;
import com.integrador.spring.app.Servicio.TorneoServices;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    @Autowired
    private TorneoServices services_torneo;

    @Autowired
    private JuegoServices services_juego;

    // listar todos los torneos
    @GetMapping("")
    public ResponseEntity<List<Torneo>> listarTorneos() {
        List<Torneo> torneos = services_torneo.listarTorneo();
        return new ResponseEntity<>(torneos, HttpStatus.OK);
    }

    // Buscar el torneo por su id;
    @GetMapping("/id/{id}")
    public ResponseEntity<Torneo> buscarTorneoId(@PathVariable Integer id) {
        Optional<Torneo> torneo = services_torneo.buscarId(id);
        return torneo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Buscar el torneo por su nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Torneo> buscarTorneoNombre(@PathVariable String nombre) {
        Optional<Torneo> torneo = services_torneo.buscarTorneoNombre(nombre);
        return torneo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //registrar un nuevo torneo
    @PostMapping("/registrar")
    public ResponseEntity<Torneo> registrarTorneo(@RequestBody Torneo torneo) {
        Torneo registrar = services_torneo.guardar(torneo);
        return new ResponseEntity<>(registrar, HttpStatus.CREATED);
    }

    //actualizar el juego por su id
    @PutMapping("/actualizar/id/{id}")
    public ResponseEntity<Torneo> actualizarId(@PathVariable Integer id, @RequestBody Torneo torneo) {
        //busca si el torneo existe
        Optional<Torneo> existe = services_torneo.buscarId(id);
        if (existe.isPresent()) {
            Torneo actualizar = existe.get();
            //si el campo no tiene nueva informacion se mantendra igual
            if (torneo.getNombreTorneo() != null) {
                actualizar.setNombreTorneo(torneo.getNombreTorneo());
            }
            if (torneo.getModoJuego() != null) {
                actualizar.setModoJuego(torneo.getModoJuego());
            }
            if (torneo.getDescripcion() != null) {
                actualizar.setDescripcion(torneo.getDescripcion());
            }
            if (torneo.getPrecioInscripcion() != null) {
                actualizar.setPrecioInscripcion(torneo.getPrecioInscripcion());
            }
            if (torneo.getPremio() != null) {
                actualizar.setPremio(torneo.getPremio());
            }
            if (torneo.getFechaFin() != null) {
                actualizar.setFechaFin(torneo.getFechaFin());
            }
            if (torneo.getEstado() != null) {
                actualizar.setEstado(torneo.getEstado());
            }
            if (torneo.getTipo() != null) {
                actualizar.setTipo(torneo.getTipo());
            }
            //si el campo no tiene nueva informacion se mantendra igual
            if (torneo.getJuego() != null && torneo.getJuego().getIdJuego() != null) {
                //busca el juego del que se equiere hacer un toreno
                Optional<Juego> juego = services_juego.buscarId(torneo.getJuego().getIdJuego());
                //si existe se agregara a la lista de torneos del juego
                if (juego.isPresent()) {
                    actualizar.setJuego(juego.get());
                } else {
                    //si no existe el juego saldra un error
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }
            //guardar la actualizacion realizarda
            return new ResponseEntity<>(services_torneo.guardar(actualizar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //actualiza el torneo por su nombre
    @PutMapping("/actualizar/nombre/{nombre}")
    public ResponseEntity<Torneo> actualizarnombreTorneo(@PathVariable String nombre, @RequestBody Torneo torneo) {
        //busca si el torneo existe
        Optional<Torneo> existe = services_torneo.buscarTorneoNombre(nombre);
        if (existe.isPresent()) {
            Torneo actualizar = existe.get();
            //si el campo no tiene nueva informacion se mantendra igual
            if (torneo.getNombreTorneo() != null) {
                actualizar.setNombreTorneo(torneo.getNombreTorneo());
            }
            if (torneo.getModoJuego() != null) {
                actualizar.setModoJuego(torneo.getModoJuego());
            }
            if (torneo.getDescripcion() != null) {
                actualizar.setDescripcion(torneo.getDescripcion());
            }
            if (torneo.getPrecioInscripcion() != null) {
                actualizar.setPrecioInscripcion(torneo.getPrecioInscripcion());
            }
            if (torneo.getPremio() != null) {
                actualizar.setPremio(torneo.getPremio());
            }
            if (torneo.getFechaFin() != null) {
                actualizar.setFechaFin(torneo.getFechaFin());
            }
            if (torneo.getEstado() != null) {
                actualizar.setEstado(torneo.getEstado());
            }
            if (torneo.getTipo() != null) {
                actualizar.setTipo(torneo.getTipo());
            }
            //si el campo no tiene nueva informacion se mantendra igual
            if (torneo.getJuego() != null && torneo.getJuego().getNombreJuego() != null) {
                //busca el juego del que se equiere hacer un toreno
                Optional<Juego> juego = services_juego.buscarJuego(torneo.getJuego().getNombreJuego());
                //si existe se agregara a la lista de torneos del juego
                if (juego.isPresent()) {
                    actualizar.setJuego(juego.get());
                } else {
                    //si no existe el juego saldra un error
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }
            //guardar la actualizacion realizarda
            return new ResponseEntity<>(services_torneo.guardar(actualizar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //elimina el torneo por su id
    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Void> eliminarTorneoId(@PathVariable Integer id) {
        try {
            services_torneo.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //elimina el torneo por su nombre
    @DeleteMapping("/eliminar/torneo/{nombre}")
    public ResponseEntity<Void> eliminarTorneoNombre(@PathVariable String nombre) {
        try {
            services_torneo.eliminarTorneoNombre(nombre);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}