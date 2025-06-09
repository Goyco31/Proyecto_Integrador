package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.PartidaRepo;
import com.integrador.spring.app.Modelo.Partida;

@Service
public class PartidaServices {

    @Autowired
    private PartidaRepo repo_partida;

    public List<Partida> listarPartidas() {
        return repo_partida.findAll();
    }

    public Optional<Partida> buscarId(int id) {
        return repo_partida.findById(id);
    }

    public Partida actualizar(int id, Partida partida) {
        Partida existe = repo_partida.findById(id)
        .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        return repo_partida.save(existe);
    }

    public Partida guardar(Partida partida){
        return repo_partida.save(partida);
    }

    public void eliminar(int id){
        repo_partida.deleteById(id);
    }
}