package com.integrador.spring.app.Database;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Modelo.Torneo.EstadoTorneo;
import com.integrador.spring.app.Modelo.Torneo.TipoTorneo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TorneoDTO {
    //Entidades
    private Integer idTorneo;
    private String nombreTorneo, modoJuego;
    private EstadoTorneo estado;
    private TipoTorneo tipo;
    private String descripcion;
    private BigDecimal precioInscripcion, premio;
    private LocalDate fechaInicio, fechaFin;
    private JuegoDTO juego;
    private List<InscripcionesDTO> inscripciones;
    private List<PartidaDTO> partidas;

    //Constructor de acceso a la tabla Torneo
    public TorneoDTO(Torneo torneo) {
        this.idTorneo = torneo.getIdTorneo();
        this.nombreTorneo = torneo.getNombreTorneo();
        this.modoJuego = torneo.getModoJuego();
        this.estado = torneo.getEstado();
        this.tipo = torneo.getTipo();
        this.descripcion = torneo.getDescripcion();
        this.precioInscripcion = torneo.getPrecioInscripcion();
        this.premio = torneo.getPremio();
        this.fechaInicio = torneo.getFechaFin();
        this.fechaFin = torneo.getFechaFin();
        this.juego = new JuegoDTO(torneo.getJuego());
        this.inscripciones = torneo.getInscripciones().stream().map(InscripcionesDTO::new).toList();
        this.partidas = torneo.getPartidas().stream().map(PartidaDTO::new).toList();
    }

}