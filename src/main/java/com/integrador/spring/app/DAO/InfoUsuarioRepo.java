package com.integrador.spring.app.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.InfoUsuario;
//repositorio de la tabla infoUsuario
@Repository
public interface InfoUsuarioRepo extends JpaRepository<InfoUsuario, Integer>{

}
