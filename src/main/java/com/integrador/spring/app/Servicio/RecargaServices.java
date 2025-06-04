package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.RecargaRepo;
import com.integrador.spring.app.Modelo.Recarga;

@Service
public class RecargaServices {
    @Autowired
    private RecargaRepo repo_recarga;

    public List<Recarga> listarRecarga() {
        return repo_recarga.findAll();
    }

    public Optional<Recarga> buscarId(int id) {
        return repo_recarga.findById(id);
    }

    public Recarga actualizar(int id, Recarga recarga) {
        Recarga existe = repo_recarga.findById(id).orElseThrow(() -> new RuntimeException("No existe la recarga"));
        return repo_recarga.save(existe);
    }

    public Recarga guardar(Recarga recarga) {
        return repo_recarga.save(recarga);
    }

    public void eliminar(int id) {
        repo_recarga.deleteById(id);
    }
}

