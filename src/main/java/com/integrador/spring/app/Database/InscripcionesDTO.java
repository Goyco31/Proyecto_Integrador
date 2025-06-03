package com.integrador.spring.app.Database;

import java.time.LocalDate;

import com.integrador.spring.app.Modelo.Inscripciones;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InscripcionesDTO {
    //Entidades
    private Integer idIncripcion;
    private LocalDate fechaIncripcion;
    private EquipoDTO equipo;
    private TorneoDTO torneo;

    //Constructor de acceso a la tabla Inscripciones
    public InscripcionesDTO(Inscripciones inscripciones) {
        this.idIncripcion = inscripciones.getIdIncripcion();
        this.fechaIncripcion = inscripciones.getFechaIncripcion();
        this.equipo = new EquipoDTO(inscripciones.getEquipo());
        this.torneo = new TorneoDTO(inscripciones.getTorneo());
    }

    

}