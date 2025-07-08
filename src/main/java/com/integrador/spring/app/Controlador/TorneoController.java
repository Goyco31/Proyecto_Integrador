package com.integrador.spring.app.Controlador;

import java.time.LocalDate;
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

import java.util.Base64;

import com.integrador.spring.app.Modelo.Inscripciones;
import com.integrador.spring.app.Modelo.Juego;
import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Modelo.Torneo.EstadoTorneo;
import com.integrador.spring.app.Modelo.Torneo.Tipo;
import com.integrador.spring.app.Servicio.TorneoServices;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    @Autowired
    private TorneoServices services_torneo;

    // listar todos los torneos
    @GetMapping("/lista")
    public ResponseEntity<List<Torneo>> listarTorneos() {
        List<Torneo> torneos = services_torneo.listarTorneo();
        return new ResponseEntity<>(torneos, HttpStatus.OK);
    }

    // Buscar el torneo por su id;
    @GetMapping("/id/{id}")
    public ResponseEntity<Torneo> buscarTorneoId(@PathVariable Integer id) {
        Optional<Torneo> torneo = services_torneo.buscarId(id);
        if (torneo.isPresent()) {
            Torneo t = torneo.get();
            System.out.println("Torneo encontrado con ID: " + id);
            System.out.println("bannerBase64: " + t.getBannerBase64());
            if (t.getJuego() != null) {
                System.out.println("juego.imgJuegoBase64: " + t.getJuego().getImgJuegoBase64());
            } else {
                System.out.println("juego is null");
            }
            return new ResponseEntity<>(t, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Buscar el torneo por su nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Torneo> buscarTorneoNombre(@PathVariable String nombre) {
        Optional<Torneo> torneo = services_torneo.buscarTorneoNombre(nombre);
        return torneo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // registrar un nuevo torneo
    @PostMapping("/registrar")
    public ResponseEntity<Torneo> registrarTorneo(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipo") Tipo tipo,
            @RequestParam("banner") MultipartFile banner,
            @RequestParam("fecha") LocalDate fecha,
            @RequestParam("cupos") Integer cupos,
            @RequestParam("premio") Integer premio,
            @RequestParam("docReglamento") MultipartFile docReglamento,
            @RequestParam("estado") EstadoTorneo estado,
            @RequestParam("juego") Juego juego) throws java.io.IOException {

        try {
            Torneo nueva = services_torneo.añadirTorneo(nombre, descripcion, tipo, banner, fecha, premio, cupos,
                    descripcion, docReglamento, estado, juego);
            return new ResponseEntity<>(nueva, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // actualizar el juego por su id
    @PutMapping("actualizar/id/{id}")
    public ResponseEntity<Torneo> actualizarTorneo(@PathVariable Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipo") Tipo tipo,
            @RequestParam("banner") MultipartFile banner,
            @RequestParam("fecha") LocalDate fecha,
            @RequestParam("cupos") Integer cupos,
            @RequestParam("premio") Integer premio,
            @RequestParam("docReglamento") MultipartFile docReglamento,
            @RequestParam("estado") EstadoTorneo estado,
            @RequestParam("juego") Juego juego) throws java.io.IOException {

        try {
            Torneo actualizar = services_torneo.actualizarTorneo(id, nombre, descripcion, tipo, banner,
                    fecha, premio,
                    cupos, descripcion,
                    docReglamento, estado, juego);
            return ResponseEntity.ok(actualizar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping("/downloadReglamento/{idTorneo}")
    public ResponseEntity<String> downloadReglamento(@PathVariable Integer idTorneo) {
        Optional<Torneo> torneo = services_torneo.buscarId(idTorneo);
        if (!torneo.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] docReglamento = torneo.get().getDocReglamento();
        if (docReglamento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String fileName = "reglamento_" + torneo.get().getNombre() + ".pdf";
        String base64Reglamento = Base64.getEncoder().encodeToString(docReglamento);

        return ResponseEntity.ok()
                .body(base64Reglamento);
    }

    /////////////////////////
    @PostMapping("/registrarEquipoTorneo")
    public ResponseEntity<Inscripciones> registrarEquipoEnTorneo(
            @RequestParam("idTorneo") Integer idTorneo,
            @RequestParam("idEquipo") Integer idEquipo) {

        try {
            Inscripciones nuevaInscripcion = services_torneo.registrarEquipoEnTorneo(idEquipo, idTorneo);
            return ResponseEntity.ok(nuevaInscripcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    ///

    // elimina el torneo por su id
    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Void> eliminarTorneoId(@PathVariable Integer id) {
        try {
            services_torneo.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // elimina el torneo por su nombre
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
