package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.EstadisticaRonda;

//manejo del CRUD de la clase estadistica con JPA
@Repository
public interface EstadisticaRondaRepo extends JpaRepository<EstadisticaRonda, Integer>{
    
}
