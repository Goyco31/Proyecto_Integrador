package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Monedas;

//manejo del CRUD de la clase monedas con JPA
@Repository
public interface MonedasRepo extends JpaRepository<Monedas, Integer>{

}
