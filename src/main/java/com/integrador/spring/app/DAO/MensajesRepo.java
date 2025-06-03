package com.integrador.spring.app.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Mensajes;

@Repository
public interface MensajesRepo extends JpaRepository<Mensajes, Integer>{
    public Optional<Mensajes> findByRazon(String razon);
}
