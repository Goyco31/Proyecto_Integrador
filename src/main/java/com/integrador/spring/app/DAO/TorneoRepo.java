package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Torneo;

//manejo del CRUD de la clase torneo con JPA
@Repository
public interface TorneoRepo extends JpaRepository<Torneo, Integer>{
    //busca por su nombre
    public Optional<Torneo> findByNombreTorneo(String nombreTorneo);
    //elimina por su nombre
    public void deleteByNombreTorneo(String nombreTorneo);
}
