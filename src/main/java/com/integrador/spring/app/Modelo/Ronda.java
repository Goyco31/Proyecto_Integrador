package com.integrador.spring.app.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo con la tabla Ronda de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Ronda {

    // id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRonda;

    // Atributos de la clase
    private int numeroRonda, puntajeEquipo1, puntajeEquipo2,
            totalKillsEquipo1, totalKillsEquipo2;

    // Relacion muchos a uno con la tabla Partida
    @ManyToOne
    private Partida partida;

    // Relacion uno a uno con la tabla Equipo
    @OneToOne
    @JoinColumn(name = "equipo_ganador", unique = false)
    private Equipo equipoGanador;

    // Relacion uno a uno con la tabla Usuario
    @OneToOne
    @JoinColumn(name = "usuario_mvp", unique = false)
    private User mvp;

    // Relacion uno a uno con la tabla EstaditicaRonda
    @OneToOne
    private EstadisticaRonda estadisticaRonda;

}