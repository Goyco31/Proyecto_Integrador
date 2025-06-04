package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.EstadisticaRondaRepo;
import com.integrador.spring.app.Modelo.EstadisticaRonda;

@Service
public class EstadisticaRondaServices {

    @Autowired
    private EstadisticaRondaRepo repo_estadistica;

    public List<EstadisticaRonda> listarEstadistica() {
        return repo_estadistica.findAll();
    }

    public Optional<EstadisticaRonda> buscarId(int id) {
        return repo_estadistica.findById(id);
    }

    public EstadisticaRonda actualizar(int id, EstadisticaRonda estadistica) {
        EstadisticaRonda existe = repo_estadistica.findById(id)
                .orElseThrow(() -> new RuntimeException("Estadistica no encontrada"));
        return repo_estadistica.save(existe);
    }

    public EstadisticaRonda guardar(EstadisticaRonda estadistica) {
        return repo_estadistica.save(estadistica);
    }

    public void eliminar(int id) {
        repo_estadistica.deleteById(id);
    }
}
