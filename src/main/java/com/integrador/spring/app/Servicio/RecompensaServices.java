package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.integrador.spring.app.DAO.RecompensaRepo;
import com.integrador.spring.app.Modelo.Recompensa;

@Service
public class RecompensaServices {
    // inyeccion de repositorio
    @Autowired
    private RecompensaRepo repo_recompensa;

    // lista todos las recompensas
    public List<Recompensa> listarTodas() {
        return repo_recompensa.findAll();
    }

    // lista las recompensas disponibles
    public List<Recompensa> listarDisponibles() {
        return repo_recompensa.findByDisponibleTrue();
    }

    // busca por su id
    public Optional<Recompensa> buscarId(Integer id) {
        return repo_recompensa.findById(id);
    }

    // crea una nueva recompensa
    public Recompensa añadirRecompensa(String nombre, String descripcion, Integer costo, Integer cantidad,
            MultipartFile imagen) throws java.io.IOException {
        Recompensa nuevo = new Recompensa();
        nuevo.setNombre(nombre);
        nuevo.setDescripcion(descripcion);
        nuevo.setCosto(costo);
        nuevo.setCantidad(cantidad);
        if (cantidad > 0) {
            nuevo.setDisponible(true);
        }
        nuevo.setDisponible(false);

        nuevo.setImgRecompensa(imagen.getBytes());
        return repo_recompensa.save(nuevo);
    }

    // guarda la informacion
    public Recompensa guardar(Recompensa recompensa) {
        return repo_recompensa.save(recompensa);
    }

    // elimina por su id
    public void eliminar(Integer id) {
        repo_recompensa.deleteById(id);
    }
}
