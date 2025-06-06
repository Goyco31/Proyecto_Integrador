package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Equipo;

//manejo del CRUD de la CLase equipo con JPA
@Repository
public interface EquipoRepo extends JpaRepository<Equipo,  Integer>{
    //busca por el nombre del equipo
    public Optional<Equipo> findBfindByNombreEquipo(String nom_equipo);
    //elimina por el nombre del equipo
    public void deleteByNombreEquipo(String nom_equipo);
}