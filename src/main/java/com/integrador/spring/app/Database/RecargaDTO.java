package com.integrador.spring.app.Database;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.integrador.spring.app.Modelo.Recarga;
import com.integrador.spring.app.Modelo.Recarga.Estado;
import com.integrador.spring.app.Modelo.Recarga.TipoPago;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecargaDTO {
    // Entidades
    private Integer idRecarga;
    private UsuarioDTO usuario;
    private LocalDate fechaRecarga;
    private int cantidad;
    private BigDecimal montoPagar;
    private TipoPago tipoPago;
    private Estado estado;

    // Constructor de acceso a la tabla Recarga
    public RecargaDTO(Recarga recarga) {
        this.idRecarga = recarga.getIdRecarga();
        this.usuario = new UsuarioDTO(recarga.getUsuario());
        this.fechaRecarga = recarga.getFechaRecarga();
        this.cantidad = recarga.getCantidad();
        this.montoPagar = recarga.getMontoPagar();
        this.tipoPago = recarga.getTipoPago();
        this.estado = recarga.getEstado();
    }

}