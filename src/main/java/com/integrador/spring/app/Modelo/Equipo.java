package com.integrador.spring.app.Modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo de la tabla Equipo de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Equipo {

    //id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEquipo;

    //Atributos de la clase
    @Column(unique = true)
    private String nombreEquipo;
    
    private LocalDate fechaCreacion;

    //Relacio uno a muchos con la tabla usuario
    @OneToMany
    private List<User> integrantes;

    //Relacio uno a uno muchos la tabla Inscripciones
    @OneToMany
    private List<Inscripciones> inscripciones;

    @ManyToOne
    private User lider;

    private String logoUrl;
    private String region;
    private String descripcion;
}
