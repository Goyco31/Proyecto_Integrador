package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.MensajesRepo;
import com.integrador.spring.app.Modelo.Mensajes;

@Service
public class MensajesServices {
    @Autowired
    private MensajesRepo repo_mensajes;

    public List<Mensajes> listarMensajes() {
        return repo_mensajes.findAll();
    }

    public Optional<Mensajes> buscarId(int id) {
        return repo_mensajes.findById(id);
    }

    public Mensajes actualizar(int id, Mensajes msj) {
        Mensajes existe = repo_mensajes.findById(id)
                .orElseThrow(() -> new RuntimeException("MEnsaje no encontrado"));
        return repo_mensajes.save(existe);
    }

    public Mensajes guardar(Mensajes msj) {
        return repo_mensajes.save(msj);
    }

    public void eliminar(int id) {
        repo_mensajes.deleteById(id);
    }
}