package com.integrador.spring.app.Modelo;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class CanjeRecompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCanje;

    @ManyToOne
    //@JsonBackReference
    private User usuario;

    @ManyToOne
    private Recompensa recompensa;

    @CreationTimestamp
    private LocalDateTime fechaCanje;
}
