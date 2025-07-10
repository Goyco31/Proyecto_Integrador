package com.integrador.spring.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Genera automáticamente getters, setters, toString, equals y hashCode
@Data

// Permite construir objetos usando el patrón Builder
@Builder

// Constructor con todos los argumentos
@AllArgsConstructor

// Constructor sin argumentos
@NoArgsConstructor
public class MiembroDTO {

    // Apodo o alias del miembro
    private String nickname;

    // URL o nombre del archivo de la foto de perfil del miembro
    // (Esto asume que existe un campo similar en tu entidad User)
    private String fotoPerfil; // si lo tienes en tu modelo User
}
