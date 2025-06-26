package com.integrador.spring.app.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipoResponseDTO {
    private Integer id;
    private String nombre;
    private String logoUrl;
    private String region;
    private String descripcion;
    private String liderNickname;
    private int cantidadMiembros;
    private List<MiembroDTO> integrantes;
}