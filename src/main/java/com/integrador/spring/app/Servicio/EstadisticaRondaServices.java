package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.EstadisticaRondaRepo;
import com.integrador.spring.app.Modelo.EstadisticaRonda;

//servicios que se usaran para el controlador de la estadistica  las rondas
@Service
public class EstadisticaRondaServices {

    //inyeccion de repositorio
    @Autowired
    private EstadisticaRondaRepo repo_estadistica;

    //lista todos
    public List<EstadisticaRonda> listarEstadistica() {
        return repo_estadistica.findAll();
    }

    //busca por su id
    public Optional<EstadisticaRonda> buscarId(int id) {
        return repo_estadistica.findById(id);
    }

    //actualiza
    public EstadisticaRonda actualizar(int id, EstadisticaRonda estadistica) {
        EstadisticaRonda existe = repo_estadistica.findById(id)
                .orElseThrow(() -> new RuntimeException("Estadistica no encontrada"));
        return repo_estadistica.save(existe);
    }

    //guarda los datos
    public EstadisticaRonda guardar(EstadisticaRonda estadistica) {
        return repo_estadistica.save(estadistica);
    }

    //elimina por su id
    public void eliminar(int id) {
        repo_estadistica.deleteById(id);
    }
}
