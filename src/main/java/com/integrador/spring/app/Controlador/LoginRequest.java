package com.integrador.spring.app.Controlador;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequest {
    String nickname;
    String contraseña;
}
