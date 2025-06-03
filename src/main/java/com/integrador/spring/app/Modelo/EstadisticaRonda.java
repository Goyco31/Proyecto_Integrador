package com.integrador.spring.app.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

//mapeo de la tabla EstadisticaRonda de la base de datos
@Data
@NoArgsConstructor
@Entity
public class EstadisticaRonda {

    // id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEstadisticaRonda;

    // Atributos de la clase
    private int kills, asistencias, muertes;
    private String rolPersonaje;
    private Boolean es_mvp;

    // Relacio uno a uno con la tabla ronda
    @OneToOne
    private Ronda ronda;

    // Relacio uno a uno con la tabla usuario
    @OneToOne
    private User usuario;

}