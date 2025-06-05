package com.integrador.spring.app.Controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.spring.app.Modelo.Equipo;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.EquipoServices;
import com.integrador.spring.app.Servicio.UsuarioServices;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioServices service_user;

    @Autowired
    private EquipoServices services_equipo;

    // Mostar todos los usuarios existentes
    @GetMapping("")
    public ResponseEntity<List<User>> listarTodo() {
        List<User> user = service_user.listarUsuarios();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Buscar usuario por su id, nickname o correo
    @GetMapping("/id/{id}")
    public ResponseEntity<User> buscarId(@PathVariable Integer id) {
        Optional<User> user = service_user.buscarId(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/nickname/{nick}")
    public ResponseEntity<User> buscarNickname(@PathVariable String nick) {
        Optional<User> user = service_user.buscarNickname(nick);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Actualizar a un usuario por su id
    @PutMapping("/actualizar/id/{id}")
    public ResponseEntity<User> actualizarId(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> existe = service_user.buscarId(id);
        if (existe.isPresent()) {
            User actualizar = existe.get();
            if (user.getNombre() != null) {
                actualizar.setNombre(user.getNombre());
            }
            if (user.getApellido() != null) {
                actualizar.setApellido(user.getApellido());
            }
            if (user.getNickname() != null) {
                actualizar.setNickname(user.getNickname());
            }
            if (user.getCorreo() != null && !user.getCorreo().equals(actualizar.getCorreo())) {
                if (service_user.buscarCorreo(user.getCorreo())) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
                actualizar.setCorreo(user.getCorreo());
            }
            if (user.getContraseña() != null) {
                actualizar.setContraseña(user.getContraseña());
            }
            if (user.getEquipo() != null && user.getEquipo().getIdEquipo() != null) {
               Optional<Equipo> equipo = services_equipo.buscarId(user.getEquipo().getIdEquipo());
                if (equipo.isPresent()) {
                    Equipo cant = equipo.get();
                    if (cant.getUsuario().size() >= 5) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                    actualizar.setEquipo(equipo.get());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }
            return new ResponseEntity<>(service_user.guardar(actualizar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar a un usuario por su Nickname
    @PutMapping("/actualizar/nickname/{nick}")
    public ResponseEntity<User> actualizarNickname(@PathVariable String nick, @RequestBody User user) {
        Optional<User> existe = service_user.buscarNickname(nick);
        if (existe.isPresent()) {
            User actualizar = existe.get();
            if (user.getNombre() != null) {
                actualizar.setNombre(user.getNombre());
            }
            if (user.getApellido() != null) {
                actualizar.setApellido(user.getApellido());
            }
            if (user.getNickname() != null) {
                actualizar.setNickname(user.getNickname());
            }
            if (user.getCorreo() != null && !user.getCorreo().equals(actualizar.getCorreo())) {
                if (service_user.buscarCorreo(user.getCorreo())) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
                actualizar.setCorreo(user.getCorreo());
            }
            if (user.getContraseña() != null) {
                actualizar.setContraseña(user.getContraseña());
            }
            if (user.getEquipo() != null && user.getEquipo().getNombreEquipo() != null) {
                Optional<Equipo> equipo = services_equipo.buscarNombreEquipo(user.getEquipo().getNombreEquipo());
                if (equipo.isPresent()) {
                    Equipo cant = equipo.get();
                    if (cant.getUsuario().size() >= 5) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                    actualizar.setEquipo(equipo.get());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }
            return new ResponseEntity<>(service_user.guardar(actualizar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar usuario por su id
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> eliminarUsuarioId(@PathVariable Integer id) {
        try {
            service_user.eliminarId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/nickname/{nick}")
    public ResponseEntity<Void> eliminarUsuarioNickname(@PathVariable String nick) {
        try {
            service_user.eliminarNickname(nick);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
