package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.User;

@Service
public class UsuarioServices {

    @Autowired
    private UserDAO repo_usuario;


    public List<User> listarUsuarios() {
        return repo_usuario.findAll();
    }

    public Optional<User> buscarId(Integer id) {
        return repo_usuario.findById(id);
    }

    public Optional<User> buscarNickname(String nick) {
        return repo_usuario.findByNickname(nick);
    }

    public boolean buscarCorreo(String correo) {
        return repo_usuario.existsByCorreo(correo);
    }

    public User actualizar(Integer id, User user) {
        User existe = repo_usuario.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return repo_usuario.save(existe);
    }

    public User guardar(User user) {
        return repo_usuario.save(user);
    }

    public void eliminarId(Integer id) {
        repo_usuario.deleteById(id);
    }

    public void eliminarNickname(String nick) {
        repo_usuario.deleteByNickname(nick);
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