package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.MonedasRepo;
import com.integrador.spring.app.Modelo.Monedas;

@Service
public class MonedasServices {
    //inyeccion de repositorio
    @Autowired
    private MonedasRepo repo_monedas;

    //lista todas las monedas
    public List<Monedas> listarMonedas(){
        return repo_monedas.findAll();
    }

    //busca por su id
    public Optional<Monedas> buscarId(int id){
        return repo_monedas.findById(id);
    }

    //actualiza por su id
    public Monedas actualizar(int id, Monedas monedas){
        Monedas existe = repo_monedas.findById(id).orElseThrow(() -> new RuntimeException("Informacion no encontrada"));
        return repo_monedas.save(existe);
    }

    //guarda la informacion
    public Monedas guardar(Monedas monedas){
        return repo_monedas.save(monedas);
    }

    //elimina por su id
    public void eliminar(int id){
        repo_monedas.deleteById(id);
    }
}