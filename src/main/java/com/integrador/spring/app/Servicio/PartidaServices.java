package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.PartidaRepo;
import com.integrador.spring.app.Modelo.Partida;

@Service
public class PartidaServices {

    //inyeccion de repositorio
    @Autowired
    private PartidaRepo repo_partida;

    //lista todo
    public List<Partida> listarPartidas() {
        return repo_partida.findAll();
    }

    //busca por id
    public Optional<Partida> buscarId(int id) {
        return repo_partida.findById(id);
    }

    //actualiza por su id
    public Partida actualizar(int id, Partida partida) {
        Partida existe = repo_partida.findById(id)
        .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        return repo_partida.save(existe);
    }

    //guarda informacion
    public Partida guardar(Partida partida){
        return repo_partida.save(partida);
    }

    //elimina por su id
    public void eliminar(int id){
        repo_partida.deleteById(id);
    }
}