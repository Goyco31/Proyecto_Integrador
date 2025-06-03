package com.integrador.spring.app.Database;

import java.time.LocalDate;
import java.util.List;

import com.integrador.spring.app.Modelo.Partida;
import com.integrador.spring.app.Modelo.Partida.EstadoPartida;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartidaDTO {
    //Entidades
    private int idPartida;
    private LocalDate fechaPartida;
    private EstadoPartida estado;
    private TorneoDTO torneo;
    private EquipoDTO equipo1;
    private EquipoDTO equipo2;
    private List<RondaDTO> ronda;

    //Constructor de acceso a la tabla partida
    public PartidaDTO(Partida partida) {
        this.idPartida = partida.getIdPartida();
        this.fechaPartida = partida.getFechaPartida();
        this.estado = partida.getEstado();
        this.torneo = new TorneoDTO(partida.getTorneo());
        this.equipo1 = new EquipoDTO(partida.getEquipo1());
        this.equipo2 = new EquipoDTO(partida.getEquipo2());
        this.ronda = partida.getRonda().stream().map(RondaDTO::new).toList();
    }

}
