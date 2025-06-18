package com.integrador.spring.app.Controlador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import com.integrador.spring.app.DTO.UsuarioDTO;
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

    // Obtener el usuario actualmente autenticado
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getAuthenticatedUser(Authentication authentication) {
        String nickname = authentication.getName();

        Optional<User> userOpt = service_user.buscarNickname(nickname);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UsuarioDTO dto = UsuarioDTO.from(userOpt.get());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioDTO> actualizarPerfil(@RequestBody UsuarioDTO dto, Authentication auth) {
        String nicknameActual = auth.getName();
        Optional<User> userOpt = service_user.buscarNickname(nicknameActual);

        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User user = userOpt.get();

        // Actualiza los campos si vienen no nulos
        if (dto.nombre() != null) user.setNombre(dto.nombre());
        if (dto.apellido() != null) user.setApellido(dto.apellido());
        if (dto.nickname() != null) user.setNickname(dto.nickname());
        if (dto.correo() != null) user.setCorreo(dto.correo());
        if (dto.fotoPerfil() != null) user.setFotoPerfil(dto.fotoPerfil());

        User actualizado = service_user.guardar(user);
        return ResponseEntity.ok(UsuarioDTO.from(actualizado));
    }


    @PostMapping("/api/usuarios/upload-foto")
    public ResponseEntity<String> uploadFoto(@RequestParam("file") MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.matches(".*\\.(jpg|jpeg|png)$")) {
                return ResponseEntity.badRequest().body("Formato no permitido");
            }

            String filename = UUID.randomUUID() + "_" + originalName;
            Path path = Paths.get("uploads/perfil/" + filename);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(filename); // <--- lo guardarás en la BD
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir imagen");
        }
    }


    // Mostar todos los usuarios existentes
    @GetMapping("/lista")
    public ResponseEntity<List<User>> listarTodo() {
        List<User> user = service_user.listarUsuarios();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Buscar usuario por su id
    @GetMapping("/id/{id}")
    public ResponseEntity<User> buscarId(@PathVariable Integer id) {
        Optional<User> user = service_user.buscarId(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Busca el usuarios por su nickname
    @GetMapping("/nickname/{nick}")
    public ResponseEntity<User> buscarNickname(@PathVariable String nick) {
        Optional<User> user = service_user.buscarNickname(nick);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Actualizar a un usuario por su id
    @PutMapping("/actualizar/id/{id}")
    public ResponseEntity<User> actualizarId(@PathVariable Integer id, @RequestBody User user) {
        // busca el usuario que se quiere actualizar
        Optional<User> existe = service_user.buscarId(id);
        // valida que existe el usuario
        if (existe.isPresent()) {

            User actualizar = existe.get();
            // si no se ingresa nuevo informacion en el campo se mantendra igual
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
            if (user.getMonedas() != null) {
                actualizar.setMonedas(user.getMonedas());
            }
            // si no se ingresa nuevo informacion en el campo se mantendra igual
            if (user.getEquipo() != null && user.getEquipo().getIdEquipo() != null) {
                // busca al equipo que se quiere unir
                Optional<Equipo> equipo = services_equipo.buscarId(user.getEquipo().getIdEquipo());
                if (equipo.isPresent()) {
                    Equipo cant = equipo.get();
                    // si el equipo ya tiene 5 integrantes no le dejara unirse
                    if (cant.getUsuario().size() >= 5) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                    // si aun no esta lleno se unira con exito
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
        // busca al usuario y valida que existe
        Optional<User> existe = service_user.buscarNickname(nick);
        if (existe.isPresent()) {
            User actualizar = existe.get();
            // si no se ingresa nueva informacion se mantendra igual
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
            if (user.getMonedas() != null) {
                actualizar.setMonedas(user.getMonedas());
            }
            // si no se ingresa nueva informacion se mantendra igual
            if (user.getEquipo() != null && user.getEquipo().getNombreEquipo() != null) {
                // busca el equipo al que se quiere unir
                Optional<Equipo> equipo = services_equipo.buscarNombreEquipo(user.getEquipo().getNombreEquipo());
                if (equipo.isPresent()) {
                    Equipo cant = equipo.get();
                    // si el equipo ya tiene 5 integrantes no podra unirce
                    if (cant.getUsuario().size() >= 5) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                    // si el equipo no esta lleno se unira exitosamente
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
