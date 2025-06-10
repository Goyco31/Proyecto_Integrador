package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.CanjeRecompensaRepo;
import com.integrador.spring.app.DAO.RecompensaRepo;
import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.CanjeRecompensa;
import com.integrador.spring.app.Modelo.Recompensa;
import com.integrador.spring.app.Modelo.User;

@Service
public class CanjeRecompensaServices {

    @Autowired
    private CanjeRecompensaRepo repo_canje;

    @Autowired
    private UserDAO repo_user;

    @Autowired
    private RecompensaRepo repo_recompensa;

    public List<CanjeRecompensa> listarTodas() {
        return repo_canje.findAll();
    }

    public ResponseEntity<String> canjear(Integer idUser, Integer idRecompensa) {
        Optional<User> user = repo_user.findById(idUser);
        Optional<Recompensa> recom = repo_recompensa.findById(idRecompensa);
        CanjeRecompensa nuevoCanje = new CanjeRecompensa();
        
        if (user.isEmpty() || recom.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User usuario = user.get();
        Recompensa recompensa = recom.get();
        if (!recompensa.isDisponible()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La recompensa no esta disponible");
        }

        if (usuario.getMonedas() < recompensa.getCosto()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tiene suficientes monedas");
        }
        
        //verifica la cantidad que hay de la recompensa
        if (recompensa.getCantidad() >= 1) {
            // actualizacion de las monedas del usuario
            usuario.setMonedas(usuario.getMonedas() - recompensa.getCosto());
            repo_user.save(usuario);

            // actualizacion de la cantidad de la recompensa
            recompensa.setCantidad(recompensa.getCantidad() - 1);
            repo_recompensa.save(recompensa);
            //si se acabaron las recompensas se actualizara a false
            if (recompensa.getCantidad() == 0) {
                recompensa.setDisponible(false);
            }

            // actualizacion de la tabla canje
            nuevoCanje.setUsuario(usuario);
            nuevoCanje.setRecompensa(recompensa);
            repo_canje.save(nuevoCanje);

            return ResponseEntity.ok("Recompensa canjeada");
        } else{
            recompensa.setDisponible(false);
            repo_recompensa.save(recompensa);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya no esta dispobible");
        }
        
    }
}
