package com.integrador.spring.app.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrador.spring.app.Modelo.Recompensa;

//repositorio de la tabla recompensa
@Repository
public interface RecompensaRepo extends JpaRepository<Recompensa, Integer> {
    //lista las recompensas disponibles
    public List<Recompensa> findByDisponibleTrue();
}
