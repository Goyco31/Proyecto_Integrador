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

    //id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idJuego;

    //Atributos de la clase
    @Column(unique = true)
    private String nombreJuego;
    private String generoJuego;
    @Lob
    private byte[] imgJuego;

    //Relacion uno a muchos con la tabla torneo
    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Torneo> torneo;
}
