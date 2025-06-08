package com.integrador.spring.app.Modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo de la tabla Partida de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Partida {

    //id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPartida;
    
    //Atributos de la clase
    private LocalDate fechaPartida;
    private EstadoPartida estado;

    // Relacion muchos a uno con la tabla Torneo
    @ManyToOne
    private Torneo torneo;

    // Relacion muchos a uno con la tabla Equipo
    @ManyToOne
    private Equipo equipo1;

    // Relacion muchos a uno con la tabla Equipo
    @ManyToOne
    private Equipo equipo2;

    // Relacion uno a muchos con la tabla ronda
    @OneToMany
    private List<Ronda> ronda;

    //variables permitidas para el estado de la partida
    public static enum EstadoPartida {
        ACTIVO, FINALIZADO;
    }
}