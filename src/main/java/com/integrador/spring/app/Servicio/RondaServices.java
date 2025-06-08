package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.RondaRepo;
import com.integrador.spring.app.Modelo.Ronda;

@Service
public class RondaServices {
    @Autowired
    private RondaRepo repo_ronda;

    public List<Ronda> listarRonda() {
        return repo_ronda.findAll();
    }

    public Optional<Ronda> buscarId(int id) {
        return repo_ronda.findById(id);
    }

    public Ronda actualizar(int id, Ronda ronda) {
        Ronda existe = repo_ronda.findById(id)
        .orElseThrow(() -> new RuntimeException("Ronda no encontrada"));
        return repo_ronda.save(existe);
    }

    public Ronda guardar(Ronda ronda){
        return repo_ronda.save(ronda);
    }

    public void eliminar(int id){
        repo_ronda.deleteById(id);
    }
}
