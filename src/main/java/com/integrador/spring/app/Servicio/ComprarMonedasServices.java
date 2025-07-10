package com.integrador.spring.app.Servicio;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.integrador.spring.app.DAO.ComprarMonedasRepo;
import com.integrador.spring.app.Modelo.ComprarMonedas;

@Service
public class ComprarMonedasServices {
    //inyecion de repositorio
    @Autowired
    private ComprarMonedasRepo repo_comprar;

    //lista todas las opciones de recarga
    public List<ComprarMonedas> listar() {
        return repo_comprar.findAll();
    }

    //busca una opcion de recarga por su id
    public Optional<ComprarMonedas> buscarId(Integer id) {
        return repo_comprar.findById(id);
    }

    //metodo para que se registre una nueva opcion
    public ComprarMonedas añadirCompra(String nombre, Integer cantidad, BigDecimal precio, MultipartFile imgMoneda)
            throws IOException {
        ComprarMonedas opcion = new ComprarMonedas();
        opcion.setNombre(nombre);
        opcion.setCantidad(cantidad);
        opcion.setPrecioCompra(precio);
        opcion.setImgMoneda(imgMoneda.getBytes());
        return repo_comprar.save(opcion);
    }

    //actualiza una opcion de recarga
    public ComprarMonedas actualizarCompra(Integer id, String nombre, Integer cantidad, BigDecimal precio,
            MultipartFile imgMoneda) throws IOException {

        //verifica que la opcion exista
        Optional<ComprarMonedas> existe = repo_comprar.findById(id);
        if (existe.isPresent()) {
            ComprarMonedas actualizar = existe.get();
            actualizar.setNombre(nombre);
            actualizar.setCantidad(cantidad);
            actualizar.setPrecioCompra(precio);
            //valida que la imagen sea ingresada
            if (imgMoneda != null && !imgMoneda.isEmpty()) {
                actualizar.setImgMoneda(imgMoneda.getBytes());
            }
            return repo_comprar.save(actualizar);
        } else{
            return null;
        }

    }

    //guarda los datos
    public ComprarMonedas guardar(ComprarMonedas comprarMonedas) {
        return repo_comprar.save(comprarMonedas);
    }

    //elimina por su id
    public void eliminar(Integer id) {
        repo_comprar.deleteById(id);
    }
}
