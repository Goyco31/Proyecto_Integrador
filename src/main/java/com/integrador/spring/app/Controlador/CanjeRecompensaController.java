package com.integrador.spring.app.Controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integrador.spring.app.DAO.RecompensaRepo;
import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.CanjeRecompensa;
import com.integrador.spring.app.Modelo.Recompensa;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.CanjeRecompensaServices;
import com.integrador.spring.app.Servicio.EmailService;

@RestController
@RequestMapping("/api/canje")
public class CanjeRecompensaController {
    //inyeciones de las clases necesarias
    @Autowired
    private CanjeRecompensaServices services_canjes;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserDAO userRepository;
    @Autowired
    private RecompensaRepo recompensaRepository;

    //muestra todas las recompensas canjeadas
    @GetMapping("")
    public ResponseEntity<List<CanjeRecompensa>> listar() {
        List<CanjeRecompensa> todas = services_canjes.listarTodas();
        return new ResponseEntity<>(todas, HttpStatus.OK);
    }

    //proceso para que los usuarioes canjeen sus recompensas
    @PostMapping("/usuario/{idUser}/recompensa/{idRecompensa}")
    public ResponseEntity<String> canjearRecompensa(@PathVariable Integer idUser, @PathVariable Integer idRecompensa) {
        ResponseEntity<String> result = services_canjes.canjear(idUser,
                idRecompensa);

        //si el proceso sale correctamente enviara un mensaje a su correo
        if (result.getStatusCode() == HttpStatus.OK) {
            User user = userRepository.findById(idUser).orElse(null);
            Recompensa recompensa = recompensaRepository.findById(idRecompensa).orElse(null);

            //validacion de que el usuario y recompensa exista
            if (user != null && recompensa != null) {
                emailService.recompensaEmail(user.getEmail(),
                        recompensa.getNombre(), recompensa.getDescripcion());
            }
        }

        return result;
    }

}
