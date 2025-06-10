package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.MonedasRepo;
import com.integrador.spring.app.Modelo.Monedas;

@Service
public class MonedasServices {
    @Autowired
    private MonedasRepo repo_monedas;

    public List<Monedas> listarMonedas(){
        return repo_monedas.findAll();
    }

    public Optional<Monedas> buscarId(int id){
        return repo_monedas.findById(id);
    }

    public Monedas actualizar(int id, Monedas monedas){
        Monedas existe = repo_monedas.findById(id).orElseThrow(() -> new RuntimeException("Informacion no encontrada"));
        return repo_monedas.save(existe);
    }

    public Monedas guardar(Monedas monedas){
        return repo_monedas.save(monedas);
    }

    public void eliminar(int id){
        repo_monedas.deleteById(id);
    }
}