package com.integrador.spring.app.Modelo;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @CreationTimestamp
    private LocalDate fechaCreacion;

    //Relacio uno a muchos con la tabla usuario
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<User> usuario;

    //Relacio uno a uno muchos la tabla Inscripciones
    @OneToMany //(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    private List<Inscripciones> inscripciones;
}
