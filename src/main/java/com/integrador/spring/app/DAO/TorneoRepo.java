package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Torneo;

//repositorio de la tabla torneo
@Repository
public interface TorneoRepo extends JpaRepository<Torneo, Integer>{
    //busca el torneo por su nombre
    public Optional<Torneo> findByNombre(String nombreTorneo);
    //elimina el torneo por su nombre
    public void deleteByNombre(String nombreTorneo);
}
