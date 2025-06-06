package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.InfoUsuario;

//manejo del CRUD de la clase infoUsuario con JPA
@Repository
public interface InfoUsuarioRepo extends JpaRepository<InfoUsuario, Integer>{

}
