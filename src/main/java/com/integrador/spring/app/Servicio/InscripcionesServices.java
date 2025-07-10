package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.InscripcionesRepo;
import com.integrador.spring.app.Modelo.Inscripciones;

@Service
public class InscripcionesServices {

    //inyecion de repositorio
    @Autowired
    private InscripcionesRepo repo_inscripciones;

    //lista todas las inscripciones
    public List<Inscripciones> listarInscripciones() {
        return repo_inscripciones.findAll();
    }

    //busca por su id
    public Optional<Inscripciones> buscarId(int id) {
        return repo_inscripciones.findById(id);
    }

    //actualiza por su id
    public Inscripciones actualizar(int id, Inscripciones inscripciones) {
        Inscripciones existe = repo_inscripciones.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripcion no encontrada"));
        return repo_inscripciones.save(existe);
    }

    //guarda la informacion
    public Inscripciones guardar(Inscripciones inscripciones){
        return repo_inscripciones.save(inscripciones);
    }

    //elimina la inscripcion
    public void eliminar(int id){
        repo_inscripciones.deleteById(id);
    }
}
