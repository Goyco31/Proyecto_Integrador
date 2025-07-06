package com.integrador.spring.app.Controlador;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.integrador.spring.app.DTO.EquipoRequest;
import com.integrador.spring.app.DTO.EquipoResponseDTO;
import com.integrador.spring.app.Modelo.Equipo;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.EquipoServices;
import com.integrador.spring.app.Servicio.UsuarioServices;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private EquipoServices services_equipo;

    @Autowired
    private UsuarioServices userService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearEquipo(@RequestBody EquipoRequest request, Principal principal) {
        try {
            User creador = userService.obtenerPorNickname(principal.getName());
            Equipo nuevo = services_equipo.crearEquipo(
                request.getNombre(),
                request.getLogoUrl(),
                request.getRegion(),
                request.getDescripcion(),
                creador
            );

            EquipoResponseDTO response = services_equipo.mapearEquipoAResponse(nuevo);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @PostMapping("/unirse/{id}")
    public ResponseEntity<?> unirseAEquipo(@PathVariable Integer id, Principal principal) {
        try {
            Equipo equipo = services_equipo.buscarId(id).orElseThrow(() -> new NoSuchElementException("Equipo no encontrado"));
            User usuario = userService.obtenerPorNickname(principal.getName());
            Equipo actualizado = services_equipo.unirseAEquipo(equipo, usuario);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (IllegalStateException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("")
    public ResponseEntity<List<EquipoResponseDTO>> listarEquipos() {
        List<Equipo> equipos = services_equipo.listarEquipos();
        List<EquipoResponseDTO> response = equipos.stream()
            .map(services_equipo::mapearEquipoAResponse)
            .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // buscar equipo por su id
    @GetMapping("/id/{id}")
    public ResponseEntity<EquipoResponseDTO> buscarEquipoId(@PathVariable Integer id) {
        Optional<Equipo> equipo = services_equipo.buscarId(id);
        if (equipo.isPresent()) {
            EquipoResponseDTO dto = services_equipo.mapearEquipoAResponse(equipo.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // buscar equipo por su nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Equipo> buscarEquipoNombre(@PathVariable String nombre) {
        Optional<Equipo> equipo = services_equipo.buscarNombreEquipo(nombre);
        return equipo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // registra un nuevo equipo
    @PostMapping("/registrar")
    public ResponseEntity<Equipo> registrarEquipo(@RequestBody Equipo equipo) {
        Equipo registrar = services_equipo.guardar(equipo);
        return new ResponseEntity<>(registrar, HttpStatus.CREATED);
    }

    // actualiza la informacion del equipo por su id
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

    // actualiza el equipo por su nombre
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

    // elimina el juego por su id
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> eliminarEquipoId(@PathVariable Integer id) {
        try {
            services_equipo.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // elimina el juego por su nombre
    @DeleteMapping("/nombre/{nombre}")
    public ResponseEntity<Void> eliminarEquipoNombre(@PathVariable String nombre) {
        try {
            services_equipo.eliminarNombre(nombre);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}