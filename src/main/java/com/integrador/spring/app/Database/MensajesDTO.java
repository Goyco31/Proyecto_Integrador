package com.integrador.spring.app.Database;

import java.time.LocalDate;

import com.integrador.spring.app.Modelo.Mensajes;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MensajesDTO {
    //Entidades
    private int idMensaje;
    private UsuarioDTO emisor;
    private UsuarioDTO receptor;
    private String contenido;
    private LocalDate fechaEnvio;
    private Boolean leido;

    //Constructor de acceso a la tabla Mensajes
    public MensajesDTO(Mensajes mensajes) {
        this.idMensaje = mensajes.getIdMensaje();
        this.emisor = new UsuarioDTO(mensajes.getEmisor());
        this.receptor = new UsuarioDTO(mensajes.getReceptor());
        this.contenido = mensajes.getContenido();
        this.fechaEnvio = mensajes.getFechaEnvio();
        this.leido = mensajes.getLeido();
    }

    

}

