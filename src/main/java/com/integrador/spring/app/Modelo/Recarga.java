package com.integrador.spring.app.Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

//Mapeo de la tabla Recarga con la base de datos
@Data
@NoArgsConstructor
@Entity
public class Recarga {

    // id autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRecarga;

    //Atributos de la clase
    @CreationTimestamp
    private LocalDateTime fechaRecarga;
    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;
    @Enumerated(EnumType.STRING)
    private Estado estado;


    // Relacion muchos a uno con la tabla Usuario
    @ManyToOne
    private User usuario;

    @ManyToOne
    private ComprarMonedas comprarMonedas;

    //variables permitidas para el tipo de pago
    public static enum TipoPago {
        PAYPAL;
    }

    //Variables permitidas para el estado de la clase
    public static enum Estado {
        PENDIENTE,
        CANCELADO;
    }
}
