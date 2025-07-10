package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.EstadisticaRonda;

//repositorio de la tabla estadistica de una ronda de torneo
@Repository
public interface EstadisticaRondaRepo extends JpaRepository<EstadisticaRonda, Integer>{
    
}
