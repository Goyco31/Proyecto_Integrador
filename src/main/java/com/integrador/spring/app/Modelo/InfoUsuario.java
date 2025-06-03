package com.integrador.spring.app.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

//mapeo de la tabla Infousuario de la base de datos
@Data
@NoArgsConstructor
@Entity
public class InfoUsuario {

    //id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInfo;

    //Atributos de la clase
    private byte[] imgPerfil, imgBanner;
    private String discord, youtube, twitch, kick;

    //Relacio uno a uno con la tabla usuario
    @OneToOne
    private User usuario;
}