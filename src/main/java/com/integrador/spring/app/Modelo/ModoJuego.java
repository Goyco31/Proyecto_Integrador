package com.integrador.spring.app.Modelo;

import lombok.Getter;

public enum ModoJuego {
    PRESENCIAL("Presencial", "Se juega en ubicación física"),
    VIRTUAL("Virtual", "En línea");

    @Getter private final String nombre;
    @Getter private final String descripcion;

    ModoJuego(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}