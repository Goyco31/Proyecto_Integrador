package com.integrador.spring.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Anotación de Lombok que genera automáticamente getters, setters, toString, equals y hashCode
@Data

// Anotación de Lombok para usar el patrón Builder (creación flexible de objetos)
@Builder

// Genera un constructor con todos los campos como parámetros
@AllArgsConstructor

// Genera un constructor sin argumentos (necesario para algunas operaciones de deserialización)
@NoArgsConstructor
public class Validate2FARequest {

    // Código de verificación del segundo factor (generalmente un código de 6 dígitos)
    private String twoFactorCode;

    // Token temporal emitido previamente, necesario para validar el segundo factor de autenticación
    private String tempToken;
}
