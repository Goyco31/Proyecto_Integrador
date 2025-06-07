package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.InfoUsuarioRepo;
import com.integrador.spring.app.Modelo.InfoUsuario;

@Service
public class InfoUserServices {

    @Autowired
    private InfoUsuarioRepo repo_infoUser;

    public List<InfoUsuario> listarInfo() {
        return repo_infoUser.findAll();
    }

    public Optional<InfoUsuario> buscarId(Integer id) {
        return repo_infoUser.findById(id);
    }

    public InfoUsuario actualizar(Integer id, InfoUsuario info){
        InfoUsuario existe = repo_infoUser.findById(id).orElseThrow(() -> new RuntimeException("Informacion no encontrada"));
        return repo_infoUser.save(existe);
    }

    public InfoUsuario guardar(InfoUsuario info){
        return repo_infoUser.save(info);
    }
    
    public void eliminar(Integer id){
        repo_infoUser.deleteById(id);
    }
}