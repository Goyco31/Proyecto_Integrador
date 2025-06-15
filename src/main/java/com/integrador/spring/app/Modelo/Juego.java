package com.integrador.spring.app.Modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo de la tabla juego de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idJuego;

    //Atributos de la clase
    @Column(unique = true)
    private String nombreJuego;
    private String generoJuego;
    @Lob
    private byte[] imgJuego;

    private String imgJuegoBase64;

    public String getImgJuegoBase64() {
        return imgJuegoBase64;
    }

    public void setImgJuegoBase64(String imgJuegoBase64) {
        this.imgJuegoBase64 = imgJuegoBase64;
    }

    //Relacion uno a muchos con la tabla torneo
    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Torneo> torneo;
}
