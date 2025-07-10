package com.integrador.spring.app.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Anotación de Lombok que genera automáticamente los métodos getters, setters, toString, equals y hashCode
@Data

// Anotación de Lombok para implementar el patrón Builder (permite crear objetos de manera más legible)
@Builder

// Anotación de Lombok que genera un constructor sin argumentos
@NoArgsConstructor

// Anotación de Lombok que genera un constructor con todos los argumentos
@AllArgsConstructor
public class EquipoResponseDTO {

    // Identificador único del equipo
    private Integer id;

    // Nombre del equipo
    private String nombre;

    // URL del logo del equipo
    private String logoUrl;

    // Región geográfica o zona a la que pertenece el equipo
    private String region;

    // Descripción general del equipo
    private String descripcion;

    // Nickname o alias del líder del equipo
    private String liderNickname;

    // Cantidad total de miembros que pertenecen al equipo
    private int cantidadMiembros;

    // Lista de miembros del equipo (usando otro DTO)
    private List<MiembroDTO> integrantes;
}