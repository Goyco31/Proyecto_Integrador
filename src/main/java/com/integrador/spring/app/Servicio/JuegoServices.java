package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.integrador.spring.app.DAO.JuegoRepo;
import com.integrador.spring.app.Modelo.Juego;

@Service
public class JuegoServices {
    // inyeccion de repositorio
    @Autowired
    private JuegoRepo repo_juego;

    // lista todos los juegos
    public List<Juego> listarJuegos() {
        return repo_juego.findAll();
    }

    // busca por su id
    public Optional<Juego> buscarId(int id) {
        return repo_juego.findById(id);
    }

    // busca por su nombre
    public Optional<Juego> buscarJuego(String juego) {
        return repo_juego.findByNombreJuego(juego);
    }

    // guarda registra un nuevo juego
    public Juego guardarJuego(String nombre, String genero, MultipartFile imagen) throws java.io.IOException {
        Juego juego = new Juego();
        juego.setNombreJuego(nombre);
        juego.setGeneroJuego(genero);
        juego.setImgJuego(imagen.getBytes());
        return repo_juego.save(juego);
    }

    // actualiza el juego
    public Juego actualizar(int id, Juego juego) {
        Juego existe = repo_juego.findById(id).orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        return repo_juego.save(existe);
    }

    //guarda la informacion
    public Juego guardar(Juego juego) {
        return repo_juego.save(juego);
    }

    //elimina por su id
    public void eliminar(int id) {
        repo_juego.deleteById(id);
    }

    //elimina por si nombre
    public void eliminarJuegoNombre(String juego) {
        repo_juego.deleteByNombreJuego(juego);
    }
}