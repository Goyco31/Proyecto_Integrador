package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Ronda;

//manejo del CRUD de la clase ronda de partida con JPA
@Repository
public interface RondaRepo extends JpaRepository<Ronda, Integer>{

}
