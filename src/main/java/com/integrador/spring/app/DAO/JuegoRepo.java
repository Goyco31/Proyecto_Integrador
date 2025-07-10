package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Juego;

//repositorio de la tabla juego
@Repository
public interface JuegoRepo extends JpaRepository<Juego, Integer>{
    //busca por el nombre del juego
    public Optional<Juego> findByNombreJuego(String nombreJuego);
    //elimina por el nombre del juego
    public void deleteByNombreJuego(String nombreJuego);
}
