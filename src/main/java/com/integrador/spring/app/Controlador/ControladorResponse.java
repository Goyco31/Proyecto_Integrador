package com.integrador.spring.app.Controlador;

// Importaciones de Lombok para generar automáticamente código repetitivo
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;// Permite usar el patrón Builder para crear instancias

// Anotaciones de Lombok para reducir la escritura de código estándar
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ControladorResponse {
    String mensaje;
    // Campo que representa el token JWT generado y devuelto al usuario tras autenticarse
    String token;
    boolean requires2fa; // Nuevo campo para indicar si requiere segundo factor
    String tempToken; // Token temporal para mantener la sesión durante 2FA
    Integer idUser;
    Integer idEquipo;
}
