package com.integrador.spring.app.Database;

import com.integrador.spring.app.Modelo.EstadisticaRonda;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EstadisticaRondaDTO {
    //Entidades
    private int idEstadisticaRonda;
    private RondaDTO ronda;
    private UsuarioDTO usuario;
    private int kills, asistencias, muertes;
    private String rolPersonaje;
    private Boolean es_mvp;

    //Constructor de acceso a la tabla EstadisticaRonda
    public EstadisticaRondaDTO(EstadisticaRonda estadistica) {
        this.idEstadisticaRonda = estadistica.getIdEstadisticaRonda();
        this.ronda = new RondaDTO(estadistica.getRonda());
        this.usuario = new UsuarioDTO(estadistica.getUsuario());
        this.kills = estadistica.getKills();
        this.asistencias = estadistica.getAsistencias();
        this.muertes = estadistica.getMuertes();
        this.rolPersonaje = estadistica.getRolPersonaje();
        this.es_mvp = estadistica.getEs_mvp();
    }

}