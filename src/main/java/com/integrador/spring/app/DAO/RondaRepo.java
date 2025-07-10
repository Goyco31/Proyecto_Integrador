package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Ronda;
//repositorio de la tabla ronda
@Repository
public interface RondaRepo extends JpaRepository<Ronda, Integer>{

}
