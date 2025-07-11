package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.ComprarMonedas;

//repositorio de la tabla compra de monedas
@Repository
public interface ComprarMonedasRepo extends JpaRepository<ComprarMonedas, Integer>{

}
