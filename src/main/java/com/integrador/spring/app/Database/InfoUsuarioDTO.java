package com.integrador.spring.app.Database;

import com.integrador.spring.app.Modelo.InfoUsuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoUsuarioDTO {
    //Entidades
    private int idInfo;
    private UsuarioDTO usuario;
    private byte[] imgPerfil, imgBanner;
    private String discord, youtube, twitch, kick;

    //Constructor de acceso a la tabla InfoUsuario
    public InfoUsuarioDTO(InfoUsuario info) {
        this.idInfo = info.getIdInfo();
        this.imgPerfil = info.getImgPerfil();
        this.imgBanner = info.getImgBanner();
        this.discord = info.getDiscord();
        this.youtube = info.getYoutube();
        this.twitch = info.getTwitch();
        this.kick = info.getKick();
        this.usuario = new UsuarioDTO(info.getUsuario());
    }

}