package com.integrador.spring.app.Database;

import java.util.List;

import com.integrador.spring.app.Modelo.Juego;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JuegoDTO {
    //Entidades
    private int idJuego;
    private String nombreJuego;
    private String generoJuego;
    private byte[] imgJuego;
    private List<TorneoDTO> torneo;

    //Constructor de acceso a la tabla Juego
    public JuegoDTO(Juego juego) {
        this.idJuego = juego.getIdJuego();
        this.nombreJuego = juego.getNombreJuego();
        this.generoJuego = juego.getGeneroJuego();
        this.imgJuego = juego.getImgJuego();
        this.torneo = juego.getTorneo().stream().map(TorneoDTO::new).toList();
    }

}
