package com.integrador.spring.app.Database;

import java.math.BigDecimal;

import com.integrador.spring.app.Modelo.Monedas;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonedasDTO {
    // Entidades
    private Integer idMonedas;
    private BigDecimal saldo;
    private UsuarioDTO usuario;

    // Constructor de acceso a la tabla Monedas
    public MonedasDTO(Monedas monedas) {
        this.idMonedas = monedas.getIdMonedas();
        this.saldo = monedas.getSaldo();
        this.usuario = new UsuarioDTO(monedas.getUsuario());
    }

}
