package com.integrador.spring.app.Modelo;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo de la tabla Monedas de la base de datos
@Data
@NoArgsConstructor
@Entity
public class Monedas {
    //id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMonedas;

    //Atributo de la clase
    private BigDecimal saldo;

    // Relacion uno a uno con la tabla Usuario
    @OneToOne
    private User usuario;

}