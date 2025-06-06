package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Recarga;

//manejo del CRUD de la clase recarga con JPA
@Repository
public interface RecargaRepo extends JpaRepository<Recarga, Integer>{

}
