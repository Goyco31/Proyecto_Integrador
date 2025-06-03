package com.integrador.spring.app.Modelo;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo de la tabla Mensajes de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Mensajes {
    
    //id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMensaje;

    //Atributo de la tabla
    @Lob
    private String contenido;
    private LocalDate fechaEnvio;
    private Boolean leido = false;

    // Relacion uno a uno con la tabla Usuario
    @ManyToOne
    private User emisor;

    // Relacion uno a uno con la tabla Usuario
    @ManyToOne
    private User receptor;

    
}
