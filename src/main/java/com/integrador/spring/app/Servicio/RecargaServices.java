package com.integrador.spring.app.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.ComprarMonedasRepo;
import com.integrador.spring.app.DAO.RecargaRepo;
import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.ComprarMonedas;
import com.integrador.spring.app.Modelo.Recarga;
import com.integrador.spring.app.Modelo.Recarga.Estado;
import com.integrador.spring.app.Modelo.Recarga.TipoPago;
import com.integrador.spring.app.Modelo.User;

@Service
public class RecargaServices {
    @Autowired
    private RecargaRepo repo_recarga;

    @Autowired
    private UserDAO repo_user;

    @Autowired
    private ComprarMonedasRepo repo_compra;

    public List<Recarga> listarRecarga() {
        return repo_recarga.findAll();
    }

    public ResponseEntity<String> recargar(Integer idUser, Integer idCompra) {
        Optional<User> existeUser = repo_user.findById(idUser);
        Optional<ComprarMonedas> existeOpcion = repo_compra.findById(idCompra);
        Recarga nuevaRecarga = new Recarga();

        if (existeUser.isEmpty() || existeOpcion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User usuario = existeUser.get();
        ComprarMonedas comprarMonedas = existeOpcion.get();

        usuario.setMonedas(usuario.getMonedas() + comprarMonedas.getCantidad());
        repo_user.save(usuario);

        nuevaRecarga.setUsuario(usuario);
        nuevaRecarga.setComprarMonedas(comprarMonedas);
        nuevaRecarga.setTipoPago(TipoPago.PAYPAL);
        nuevaRecarga.setEstado(Estado.PENDIENTE);
        repo_recarga.save(nuevaRecarga);

        return ResponseEntity.ok("Recarga realizada");
        // return new ResponseEntity<>(HttpStatus.OK);
    }

    public Recarga guardar(Recarga recarga) {
        return repo_recarga.save(recarga);
    }

    public void eliminar(int id) {
        repo_recarga.deleteById(id);
    }
}
