package com.integrador.spring.app.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
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
    private Integer idInfo;

    //Atributos de la clase
    private byte[] imgPerfil, imgBanner;
    private String discord, youtube, twitch, kick;

    //Relacio uno a uno con la tabla usuario
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User usuario;
}