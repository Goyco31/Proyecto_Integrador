package com.integrador.spring.app.DAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.CanjeRecompensa;

//repositorio de la tabla canje de recompensas
@Repository
public interface CanjeRecompensaRepo extends JpaRepository<CanjeRecompensa, Integer>{

}
