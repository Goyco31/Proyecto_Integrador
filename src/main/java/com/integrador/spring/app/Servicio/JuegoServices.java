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
    @Autowired
    private JuegoRepo repo_juego;

    public List<Juego> listarJuegos() {
        return repo_juego.findAll();
    }

    public Optional<Juego> buscarId(int id) {
        return repo_juego.findById(id);
    }

    public Optional<Juego> buscarJuego(String juego){
        return repo_juego.findByNombreJuego(juego);
    }

    public Juego guardarJuego(String nombre, String genero, MultipartFile imagen) throws java.io.IOException {
        Juego juego = new Juego();
        juego.setNombreJuego(nombre);
        juego.setGeneroJuego(genero);
        juego.setImgJuego(imagen.getBytes());
        return repo_juego.save(juego);
    }

    public Juego actualizar(int id, Juego juego) {
        Juego existe = repo_juego.findById(id).orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        return repo_juego.save(existe);
    }

    public Juego guardar(Juego juego) {
        return repo_juego.save(juego);
    }

    public void eliminar(int id) {
        repo_juego.deleteById(id);
    }

    public void eliminarJuegoNombre(String juego){
        repo_juego.deleteByNombreJuego(juego);
    }
}