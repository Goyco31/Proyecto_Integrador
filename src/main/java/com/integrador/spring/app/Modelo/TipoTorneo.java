package com.integrador.spring.app.Modelo;

import lombok.Getter;

public enum TipoTorneo {
    GRATIS("Gratis", 0.0),
    PAGA("Pago", null); // Precio se establece después

    @Getter private final String nombre;
    @Getter private final Double precioDefault;

    TipoTorneo(String nombre, Double precioDefault) {
        this.nombre = nombre;
        this.precioDefault = precioDefault;
    }
}