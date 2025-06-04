package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.EquipoRepo;
import com.integrador.spring.app.Modelo.Equipo;

@Service
public class EquipoServices {

    @Autowired
    private EquipoRepo repo_equipo;

    public List<Equipo> listarEquipos() {
        return repo_equipo.findAll();
    }

    public Optional<Equipo> buscarId(int id) {
        return repo_equipo.findById(id);
    }

    public Optional<Equipo> buscarNombreEquipo(String nom_equipo) {
        return repo_equipo.findBfindByNombreEquipo(nom_equipo);
    }

    public Equipo actualizar(int id, Equipo equipo) {
        Equipo existe = repo_equipo.findById(id)
        .orElseThrow(() -> new RuntimeException("Equipo no entontrado"));
        return repo_equipo.save(existe);
    }

    public Equipo guardar(Equipo equipo){
        return repo_equipo.save(equipo);
    }

    public void eliminar(int id) {
        repo_equipo.deleteById(id);
    }

    public void eliminarNickname(String nom_equipo){
        repo_equipo.deleteByNombreEquipo(nom_equipo);
    }
}
