package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.InscripcionesRepo;
import com.integrador.spring.app.Modelo.Inscripciones;

@Service
public class InscripcionesServices {

    @Autowired
    private InscripcionesRepo repo_inscripciones;

    public List<Inscripciones> listarInscripciones() {
        return repo_inscripciones.findAll();
    }

    public Optional<Inscripciones> buscarId(int id) {
        return repo_inscripciones.findById(id);
    }

    public Inscripciones actualizar(int id, Inscripciones inscripciones) {
        Inscripciones existe = repo_inscripciones.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripcion no encontrada"));
        return repo_inscripciones.save(existe);
    }

    public Inscripciones guardar(Inscripciones inscripciones){
        return repo_inscripciones.save(inscripciones);
    }

    public void eliminar(int id){
        repo_inscripciones.deleteById(id);
    }

}
