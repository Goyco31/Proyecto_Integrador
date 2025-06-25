package com.integrador.spring.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiembroDTO {
    private String nickname;
    private String fotoPerfil; // si lo tienes en tu modelo User
}