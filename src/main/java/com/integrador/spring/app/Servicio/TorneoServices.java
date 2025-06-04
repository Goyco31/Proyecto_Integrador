package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.TorneoRepo;
import com.integrador.spring.app.Modelo.Torneo;

@Service
public class TorneoServices {

    @Autowired
    private TorneoRepo repo_torneo;

    public List<Torneo> listarTorneos() {
        return repo_torneo.findAll();
    }

    public Optional<Torneo> buscarId(Integer id) {
        return repo_torneo.findById(id);
    }

    public Optional<Torneo> buscarNombre(String nombre) {
        return repo_torneo.findByNombreTorneo(nombre);
    }

    public Torneo actualizar(Integer id, Torneo torneo) {
        Torneo existe = repo_torneo.findById(id)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
        return repo_torneo.save(existe);
    }

    public Torneo guardar(Torneo torneo){
        return repo_torneo.save(torneo);
    }

    public  void eliminarId(Integer id){
        repo_torneo.deleteById(id);
    }

    public void elinimarNombre(String nombre){
        repo_torneo.deleteByNombreTorneo(nombre);
    }
}
