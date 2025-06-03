package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Inscripciones;

@Repository
public interface InscripcionesRepo extends JpaRepository<Inscripciones, Integer>{

}
