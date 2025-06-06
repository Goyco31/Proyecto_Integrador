package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.EquipoRepo;
import com.integrador.spring.app.Modelo.Equipo;

//servicios que se usaran para el controlador de los equipos
@Service
public class EquipoServices {

    @Autowired
    private EquipoRepo repo_equipo;

    //listar todos los equipos
    public List<Equipo> listarEquipos() {
        return repo_equipo.findAll();
    }

    //busca un equipo por su id
    public Optional<Equipo> buscarId(int id) {
        return repo_equipo.findById(id);
    }

    //busca un equipo por su nombre
    public Optional<Equipo> buscarNombreEquipo(String nom_equipo) {
        return repo_equipo.findBfindByNombreEquipo(nom_equipo);
    }

    //actualiza un equipo por su id
    public Equipo actualizar(int id, Equipo equipo) {
        Equipo existe = repo_equipo.findById(id)
        .orElseThrow(() -> new RuntimeException("Equipo no entontrado"));
        return repo_equipo.save(existe);
    }

    //guarda la nueva informacion
    public Equipo guardar(Equipo equipo){
        return repo_equipo.save(equipo);
    }
 
    //elimina el equipo por su id
    public void eliminar(int id) {
        repo_equipo.deleteById(id);
    }

    //elimina el equipo por su nombre
    public void eliminarNombre(String nom_equipo){
        repo_equipo.deleteByNombreEquipo(nom_equipo);
    }
}
