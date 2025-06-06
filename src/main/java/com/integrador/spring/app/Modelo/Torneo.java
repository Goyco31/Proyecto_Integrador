package com.integrador.spring.app.Modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "torneo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Torneo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_torneo;
    
    Integer id_juego;

    @Column(nullable = false)
    String nombre_torneo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private TipoTorneo tipo; // ENUM: PAGA o GRATIS
    
    Double precio_inscripcion;
    
    @Column(columnDefinition = "TEXT")
    String premio;
    
    @Column(name = "modo_juego")
    private ModoJuego modoJuego;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTorneo estado; // ENUM: PROGRAMADO, EN_CURSO, FINALIZADO, CANCELADO

    @CreatedBy
    private String creador;
    
    @LastModifiedDate
    private LocalDateTime fechaModificacion;

    
}