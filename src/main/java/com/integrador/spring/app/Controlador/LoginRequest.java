package com.integrador.spring.app.Controlador;

// Importaciones de Lombok para reducir la escritura de código repetitivo
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

// Anotaciones de Lombok
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequest {
    // Campo para el nombre de usuario o identificador único (usado en el login)
    String nickname;
    // Campo para la contraseña del usuario (en texto plano al ser enviada)
    String contraseña;
}
