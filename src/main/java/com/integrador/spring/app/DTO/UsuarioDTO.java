package com.integrador.spring.app.DTO;


import com.integrador.spring.app.Modelo.InfoUsuario;
import com.integrador.spring.app.Modelo.User;

public record UsuarioDTO(
    String nombre,
    String apellido,
    String nickname,
    String correo,
    String fotoPerfil,
    Integer monedas,
    String role,
    String discord,
    String youtube,
    String twitch,
    String kick
) {
    public static UsuarioDTO from(User user) {
        InfoUsuario info = user.getInfo();

        return new UsuarioDTO(
            user.getNombre(),
            user.getApellido(),
            user.getNickname(),
            user.getCorreo(),
            user.getFotoPerfil(),
            user.getMonedas(),
            user.getRole().name(),
            info != null ? info.getDiscord() : null,
            info != null ? info.getYoutube() : null,
            info != null ? info.getTwitch() : null,
            info != null ? info.getKick() : null
        );
    }
}
