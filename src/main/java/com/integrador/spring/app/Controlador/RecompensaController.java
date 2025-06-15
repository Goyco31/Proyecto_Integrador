package com.integrador.spring.app.Controlador;

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

import com.integrador.spring.app.Modelo.Recompensa;
import com.integrador.spring.app.Servicio.RecompensaServices;

import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/recompensas")
public class RecompensaController {

    @Autowired
    private RecompensaServices services_recompensa;

    @GetMapping("/lista")
    public ResponseEntity<List<Recompensa>> listar() {
        List<Recompensa> todos = services_recompensa.listarTodas();

        for(Recompensa recompensa : todos){
            if (recompensa.getImgRecompensa() != null) {
                byte[] imgRecompensa = recompensa.getImgRecompensa();
                String imgRecompensaBase64 = Base64.getEncoder().encodeToString(imgRecompensa);
                recompensa.setImgRecompensaBase64(imgRecompensaBase64);
            }
        }
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Recompensa>> disponibles() {
        List<Recompensa> disponibles = services_recompensa.listarDisponibles();
        return new ResponseEntity<>(disponibles, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Recompensa> buscarId(@PathVariable Integer id) {
        Optional<Recompensa> existe = services_recompensa.buscarId(id);
        return existe.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Recompensa> agregarRecompensa(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("costo") Integer costo,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("imagen") MultipartFile imagen) throws java.io.IOException {

        try {
            Recompensa nueva = services_recompensa.añadirRecompensa(nombre, descripcion, costo, cantidad, imagen);
            return ResponseEntity.ok(nueva);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/actualizar/id/{id}")
    public ResponseEntity<Recompensa> actualizarRecompensa(@PathVariable Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("costo") Integer costo,
            // @RequestParam("disponible") String disponible,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("imagen") MultipartFile imagen) throws java.io.IOException {

        Optional<Recompensa> existe = services_recompensa.buscarId(id);

        if (existe.isPresent()) {
            Recompensa actualizar = existe.get();
            actualizar.setNombre(nombre);
            actualizar.setDescripcion(descripcion);
            actualizar.setCosto(costo);
            actualizar.setCantidad(cantidad);
            if (cantidad > 0) {
                actualizar.setDisponible(true);
            } else {
                actualizar.setDisponible(false);
            }
            actualizar.setImgRecompensa(imagen.getBytes());
            return new ResponseEntity<>(services_recompensa.guardar(actualizar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Void> eliminarRecompensa(@PathVariable Integer id) {
        try {
            services_recompensa.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
