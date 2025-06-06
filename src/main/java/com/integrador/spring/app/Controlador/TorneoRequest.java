package com.integrador.spring.app.Controlador;

import java.time.LocalDateTime;

import com.integrador.spring.app.Modelo.ModoJuego;
import com.integrador.spring.app.Modelo.TipoTorneo;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TorneoRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombreTorneo;
    
    @NotNull(message = "El modo de juego es requerido")
    private ModoJuego modoJuego;
    
    @NotNull(message = "El tipo de torneo es requerido")
    private TipoTorneo tipo;
    
    @PositiveOrZero(message = "El precio debe ser positivo")
    private Double precioInscripcion;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    @NotNull(message = "La fecha de inicio es requerida")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaInicio;
    
    @NotNull(message = "La fecha de fin es requerida")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaFin;
    
    private String premio;
}
