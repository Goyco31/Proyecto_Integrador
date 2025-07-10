package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.RondaRepo;
import com.integrador.spring.app.Modelo.Ronda;

@Service
public class RondaServices {
    //inyeccion de rpositorio
    @Autowired
    private RondaRepo repo_ronda;

    //lista todas las rondas
    public List<Ronda> listarRonda() {
        return repo_ronda.findAll();
    }

    //busca una ronda por su id
    public Optional<Ronda> buscarId(int id) {
        return repo_ronda.findById(id);
    }

    //actualiza por su id
    public Ronda actualizar(int id, Ronda ronda) {
        Ronda existe = repo_ronda.findById(id)
        .orElseThrow(() -> new RuntimeException("Ronda no encontrada"));
        return repo_ronda.save(existe);
    }

    //guarda la informacion
    public Ronda guardar(Ronda ronda){
        return repo_ronda.save(ronda);
    }

    //elimina por su id
    public void eliminar(int id){
        repo_ronda.deleteById(id);
    }
}
