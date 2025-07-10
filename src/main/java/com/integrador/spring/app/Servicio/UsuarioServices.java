package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.User;

@Service
public class UsuarioServices {

    //inyeccion de respositorio
    @Autowired
    private UserDAO repo_usuario;


    //lista todos los usuarios
    public List<User> listarUsuarios() {
        return repo_usuario.findAll();
    }

    //busca usuario por su id
    public Optional<User> buscarId(Integer id) {
        return repo_usuario.findById(id);
    }

    //busca usuario por su nombre
    public Optional<User> buscarNickname(String nick) {
        return repo_usuario.findByNickname(nick);
    }

    //busca sus correos
    public boolean buscarCorreo(String correo) {
        return repo_usuario.existsByCorreo(correo);
    }

    //actualiza la informacion de usuario
    public User actualizar(Integer id, User user) {
        User existe = repo_usuario.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return repo_usuario.save(existe);
    }

    //guarda informacion
    public User guardar(User user) {
        return repo_usuario.save(user);
    }

    ///elimina por su id
    public void eliminarId(Integer id) {
        repo_usuario.deleteById(id);
    }

    //elimina por su nombre
    public void eliminarNickname(String nick) {
        repo_usuario.deleteByNickname(nick);
    }

    //busca por su nombre
    public User obtenerPorNickname(String nick) {
        return buscarNickname(nick).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }


    public void asignarImagenPorDefectoSiNoTiene() {
        List<User> usuarios = repo_usuario.findAll();
        for (User u : usuarios) {
            if (u.getFotoPerfil() == null || u.getFotoPerfil().isBlank()) {
                u.setFotoPerfil("default.png");
                repo_usuario.save(u);
            }
        }
    }

}