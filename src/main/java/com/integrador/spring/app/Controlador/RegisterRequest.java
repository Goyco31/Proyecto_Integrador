package com.integrador.spring.app.Controlador;

// Importaciones de Lombok para generar automáticamente código repetitivo
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

// Anotaciones de Lombok
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    // Nombre del nuevo usuario
    String nombre;
    // Apellido del nuevo usuario
    String apellido;
    // Nombre de usuario único (nickname), usado para iniciar sesión
    String nickname;
    // Correo electrónico del usuario (también puede usarse como identificador o para notificaciones)
    String correo;
    // Contraseña del usuario, en texto plano al ser enviada
    String contraseña;
}
