package com.integrador.spring.app.Modelo;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo de la tabla Inscripciones de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Inscripciones {

    //id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idIncripcion;
    
    //Atributos de la clase
    @CreationTimestamp
    private LocalDate fechaIncripcion;

    //Relacio muchos a uno con la tabla Equipo
    @ManyToOne
    @JsonBackReference
    private Equipo equipo;

    //Relacio muchos a uno con la tabla
    @ManyToOne
    private Torneo torneo;
}
