package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Inscripciones;

//repositorio de la tabla inscripciones de un equipo a un torneo
@Repository
public interface InscripcionesRepo extends JpaRepository<Inscripciones, Integer>{

}
