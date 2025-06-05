package com.integrador.spring.app.Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo con la tabla Torneo de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Torneo {

    //Id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTorneo;

    //Entidades de la clase
    @Column(unique = true)
    private String nombreTorneo;
    private String modoJuego;
    @Lob
    private String descripcion;
    private BigDecimal precioInscripcion, premio;
    @CreationTimestamp
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    @Enumerated(EnumType.STRING)
    private EstadoTorneo estado;
    @Enumerated(EnumType.STRING)
    private TipoTorneo tipo;

    //Relacion muchos a uno con la tabla Juego
    @ManyToOne
    @JsonBackReference
    private Juego juego;

    //Relacion uno a muchos con la tabla Inscripciones
    @OneToMany
    private List<Inscripciones> inscripciones;

    //Relacion uno a muchos con la tabla Partida
    @OneToMany
    private List<Partida> partidas;

    //Variables permitidas para el atributo estado del toreno
    public static enum EstadoTorneo {
        ACTIVO, FINALIZADO, CANCELADO;
    }

    //Variables permitidas para el atributo tipo de pago
    public static enum TipoTorneo {
        GRATIS, PAGO;
    }
}