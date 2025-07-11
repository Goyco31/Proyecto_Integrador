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

import com.integrador.spring.app.Modelo.Juego;
import com.integrador.spring.app.Servicio.JuegoServices;

import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    //inyeccion de servicios
    @Autowired
    private JuegoServices service_juego;

    //lista todos los juegos
    @GetMapping("/lista")
    public ResponseEntity<List<Juego>> listarTodo() {
        List<Juego> lista = service_juego.listarJuegos();

        //decoidifica la imagen de la DB
        for (Juego juego : lista) {
            if (juego.getImgJuego() != null) {
                byte[] imgJuegoBytes = juego.getImgJuego();
                String imgJuegoBase64 = Base64.getEncoder().encodeToString(imgJuegoBytes);
                juego.setImgJuegoBase64(imgJuegoBase64);
            }
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    //busca el juego por su id
    @GetMapping("/id/{id}")
    public ResponseEntity<Juego> buscarId(@PathVariable Integer id) {
        Optional<Juego> juego = service_juego.buscarId(id);
        return juego.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //busca el juego por su nombre
    @GetMapping("/nombre/{juego}")
    public ResponseEntity<Juego> buscarJuegos(@PathVariable String juego) {
        Optional<Juego> juegos = service_juego.buscarJuego(juego);
        return juegos.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //registra un nuevo juego
    @PostMapping("/registrar")
    public ResponseEntity<Juego> registrarJuego(
            @RequestParam("nombre") String nombre,
            @RequestParam("genero") String genero,
            @RequestParam("imagen") MultipartFile imagen) throws IOException, java.io.IOException {
        try {
            //envia los parametros al metodo del servicio
            Juego juegoGuardado = service_juego.guardarJuego(nombre, genero, imagen);
            return ResponseEntity.ok(juegoGuardado);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    //actualizar el juego por su id
    @PutMapping("/actualizar/id/{id}")
    public ResponseEntity<Juego> actualizarJuegoId(@PathVariable Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("genero") String genero,
            @RequestParam("imagen") MultipartFile imagen) throws java.io.IOException {
        Optional<Juego> existe = service_juego.buscarId(id);
        //si existe los datos del juego se actualizaran
        if (existe.isPresent()) {
            Juego actualizar = existe.get();
            actualizar.setNombreJuego(nombre);
            actualizar.setGeneroJuego(genero);
            actualizar.setImgJuego(imagen.getBytes()); 
            return new ResponseEntity<>(service_juego.guardar(actualizar), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //actualizar el juego por su nombre
    @PutMapping("/actualizar/nombre/{juego}")
    public ResponseEntity<Juego> actualizarJuegoId(@PathVariable String juego,
            @RequestParam("nombre") String nombre,
            @RequestParam("genero") String genero,
            @RequestParam("imagen") MultipartFile imagen) throws java.io.IOException {
        Optional<Juego> existe = service_juego.buscarJuego(juego);
        //si existe los datos del juego se actualizaran
        if (existe.isPresent()) {
            Juego actualizar = existe.get();
            actualizar.setNombreJuego(nombre);
            actualizar.setGeneroJuego(genero);
            actualizar.setImgJuego(imagen.getBytes());
            return new ResponseEntity<>(service_juego.guardar(actualizar), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //elimina el juego por su id
    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Void> eliminarJuegoId(@PathVariable Integer id) {
        try {
            service_juego.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //elimina el juego por su nombre
    @DeleteMapping("/eliminar/nombre/{nombreJuego}")
    public ResponseEntity<Void> eliminarJuegoNombre(@PathVariable String nombreJuego){
        try {
            service_juego.eliminarJuegoNombre(nombreJuego);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}