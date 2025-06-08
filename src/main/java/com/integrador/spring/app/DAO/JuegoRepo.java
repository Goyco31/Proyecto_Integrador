package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Juego;

@Repository
public interface JuegoRepo extends JpaRepository<Juego, Integer>{
    public Optional<Juego> findByNombreJuego(String nombreJuego);
    public void deleteByNombreJuego(String nombreJuego);
}
