package com.integrador.spring.app.Modelo;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;
//mapeo de la tabla
@Data
@NoArgsConstructor
@Entity
public class ComprarMonedas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompra;
    private String nombre;
    private Integer cantidad;
    private BigDecimal precioCompra;

    @Lob
    private byte[] imgMoneda;

    private String imgimgMonedaBase64;
}