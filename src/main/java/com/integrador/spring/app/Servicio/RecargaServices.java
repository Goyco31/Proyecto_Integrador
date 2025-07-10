package com.integrador.spring.app.Servicio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    // inyeccion de repositorios
    @Autowired
    private RecargaRepo repo_recarga;

    @Autowired
    private UserDAO repo_user;

    @Autowired
    private ComprarMonedasRepo repo_compra;

    // lista todas las recargas
    public List<Recarga> listarRecarga() {
        return repo_recarga.findAll();
    }

    // metodo para que el usuario recarge sus monedas
    public ResponseEntity<String> recargar(Integer idUser, Integer idCompra) {

        Optional<User> existeUser = repo_user.findById(idUser);
        Optional<ComprarMonedas> existeOpcion = repo_compra.findById(idCompra);
        Recarga nuevaRecarga = new Recarga();

        // valida que el usuario y opcion de recarga existan
        if (existeUser.isEmpty() || existeOpcion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
        }

        // mapea el usuario y opcion de recarga
        User usuario = existeUser.get();
        ComprarMonedas comprarMonedas = existeOpcion.get();

        // suma las monedas que ya tiene el usuario con la cantidad de monedas que
        // recargo
        usuario.setMonedas(usuario.getMonedas() + comprarMonedas.getCantidad());
        repo_user.save(usuario);

        // guarda los datos de la recarga en su tabla
        nuevaRecarga.setUsuario(usuario);
        nuevaRecarga.setComprarMonedas(comprarMonedas);
        nuevaRecarga.setTipoPago(TipoPago.PAYPAL);
        nuevaRecarga.setEstado(Estado.CANCELADO);
        repo_recarga.save(nuevaRecarga);

        return ResponseEntity.ok("Recarga realizada");
    }

    // guarda la informacion
    public Recarga guardar(Recarga recarga) {
        return repo_recarga.save(recarga);
    }

    // elimina por su id
    public void eliminar(int id) {
        repo_recarga.deleteById(id);
    }

    
}
