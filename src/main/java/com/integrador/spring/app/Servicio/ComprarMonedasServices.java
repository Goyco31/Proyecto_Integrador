package com.integrador.spring.app.Servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.ComprarMonedasRepo;
import com.integrador.spring.app.Modelo.ComprarMonedas;

@Service
public class ComprarMonedasServices {
    @Autowired
    private ComprarMonedasRepo repo_comprar;

    public List<ComprarMonedas> listar(){
        return repo_comprar.findAll();
    }

    public Optional<ComprarMonedas> buscarId(Integer id){
        return repo_comprar.findById(id);
    }

    public ComprarMonedas añadirCompra(String nombre, Integer cantidad, BigDecimal precio){
        ComprarMonedas opcion = new ComprarMonedas();
        opcion.setNombre(nombre);
        opcion.setCantidad(cantidad);
        opcion.setPrecioCompra(precio);
        return repo_comprar.save(opcion);
    }

    public ComprarMonedas guardar(ComprarMonedas comprarMonedas){
        return repo_comprar.save(comprarMonedas);
    }

    public void eliminar(Integer id){
        repo_comprar.deleteById(id);
    }
}