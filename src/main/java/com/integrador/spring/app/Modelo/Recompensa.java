package com.integrador.spring.app.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Recompensa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRecompensa;

    private String nombre;
    private String descripcion;
    private Integer costo;
    private boolean disponible;
    private Integer cantidad;
    @Lob
    private byte[] imgRecompensa;
}

