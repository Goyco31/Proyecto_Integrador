package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.InfoUsuarioRepo;
import com.integrador.spring.app.Modelo.InfoUsuario;

@Service
public class InfoUserServices {

    //inyecion de repositorio
    @Autowired
    private InfoUsuarioRepo repo_infoUser;

    //lista todo el info
    public List<InfoUsuario> listarInfo() {
        return repo_infoUser.findAll();
    }

    //busca por su id
    public Optional<InfoUsuario> buscarId(Integer id) {
        return repo_infoUser.findById(id);
    }

    //actualiza
    public InfoUsuario actualizar(Integer id, InfoUsuario info){
        InfoUsuario existe = repo_infoUser.findById(id).orElseThrow(() -> new RuntimeException("Informacion no encontrada"));
        return repo_infoUser.save(existe);
    }

    //guarda la informacion
    public InfoUsuario guardar(InfoUsuario info){
        return repo_infoUser.save(info);
    }
    
    //elimina por su id
    public void eliminar(Integer id){
        repo_infoUser.deleteById(id);
    }
}