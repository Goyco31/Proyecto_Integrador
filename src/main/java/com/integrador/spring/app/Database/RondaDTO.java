package com.integrador.spring.app.Database;

import com.integrador.spring.app.Modelo.Ronda;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RondaDTO {
    //Entidades
    private int idRonda;
    private int numeroRonda, puntajeEquipo1, puntajeEquipo2,
            totalKillsEquipo1, totalKillsEquipo2;
    private PartidaDTO partida;
    private EquipoDTO equipoGanador;
    private UsuarioDTO mvp;
    private EstadisticaRondaDTO estadisticaRonda;

    //Constructor de acceso a la tabla Ronda
    public RondaDTO(Ronda ronda) {
        this.idRonda = ronda.getIdRonda();
        this.numeroRonda = ronda.getNumeroRonda();
        this.puntajeEquipo1 = ronda.getPuntajeEquipo1();
        this.puntajeEquipo2 = ronda.getPuntajeEquipo2();
        this.totalKillsEquipo1 = ronda.getTotalKillsEquipo1();
        this.totalKillsEquipo2 = ronda.getTotalKillsEquipo2();
        this.partida = new PartidaDTO(ronda.getPartida());
        this.equipoGanador = new EquipoDTO(ronda.getEquipoGanador());
        this.mvp = new UsuarioDTO(ronda.getMvp());
        this.estadisticaRonda = new EstadisticaRondaDTO(ronda.getEstadisticaRonda());
    }
    
    
}
