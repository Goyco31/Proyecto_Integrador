package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Juego;

//manejo del CRUD de la clase juego con JPA
@Repository
public interface JuegoRepo extends JpaRepository<Juego, Integer>{
    //busca por su nombre
    public Optional<Juego> findByNombreJuego(String nombreJuego);
    //elimina por su nombre
    public void deleteByNombreJuego(String nombreJuego);
}
