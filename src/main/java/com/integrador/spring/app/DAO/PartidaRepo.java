package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Partida;

//manejo del CRUD de la clase partida con JPA
@Repository
public interface PartidaRepo extends JpaRepository<Partida, Integer>{
    
}
