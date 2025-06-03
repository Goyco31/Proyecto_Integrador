package com.integrador.spring.app.Database;

import java.time.LocalDate;
import java.util.List;

import com.integrador.spring.app.Modelo.Equipo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipoDTO {
    //Entidades
    private Integer idEquipo;
    private String nombreEquipo;
    private LocalDate fechaCreacion;
    private List<UsuarioDTO> usuario;
    private List<InscripcionesDTO> inscripciones;

    //Constructor de acceso a la tabla Equipo
    public EquipoDTO(Equipo equipo) {
        this.idEquipo = equipo.getIdEquipo();
        this.nombreEquipo = equipo.getNombreEquipo();
        this.fechaCreacion = equipo.getFechaCreacion();
        this.usuario = equipo.getUsuario().stream().map(UsuarioDTO::new).toList();
        this.inscripciones = equipo.getInscripciones().stream().map(InscripcionesDTO::new).toList();
    }

}