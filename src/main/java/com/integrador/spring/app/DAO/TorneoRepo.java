package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Torneo;


@Repository
public interface TorneoRepo extends JpaRepository<Torneo, Integer>{
    public Optional<Torneo> findByNombreTorneo(String nombreTorneo);
    public void deleteByNombreTorneo(String nombreTorneo);
}
