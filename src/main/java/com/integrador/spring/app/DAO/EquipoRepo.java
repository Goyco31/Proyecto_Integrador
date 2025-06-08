package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Equipo;

@Repository
public interface EquipoRepo extends JpaRepository<Equipo,  Integer>{
    public Optional<Equipo> findBfindByNombreEquipo(String nom_equipo);
    public void deleteByNombreEquipo(String nom_equipo);
}