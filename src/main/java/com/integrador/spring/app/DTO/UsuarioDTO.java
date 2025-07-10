package com.integrador.spring.app.DTO;


import com.integrador.spring.app.Modelo.InfoUsuario;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Modelo.Equipo;

// Declaración de un record en Java (desde Java 14+), que representa un DTO (Data Transfer Object) para la entidad Usuario
public record UsuarioDTO(

    // Nombre del usuario
    String nombre,

    // Apellido del usuario
    String apellido,

    // Apodo o alias único del usuario
    String nickname,

    // Correo electrónico del usuario
    String correo,

    // Ruta o URL de la imagen de perfil del usuario
    String fotoPerfil,

    // Cantidad de monedas virtuales del usuario
    Integer monedas,

    // Rol del usuario (ej. ADMIN, USER, etc.)
    String role,

    // Nombre de usuario en Discord (opcional)
    String discord,

    // Canal de YouTube del usuario (opcional)
    String youtube,

    // Canal de Twitch del usuario (opcional)
    String twitch,

    // Perfil en Kick del usuario (opcional)
    String kick,

    // Objeto que representa el equipo al que pertenece el usuario
    Equipo equipo

) {
    // Método estático que construye un UsuarioDTO a partir de una entidad User
    public static UsuarioDTO from(User user) {
        // Se obtiene la información adicional del usuario (redes sociales, etc.)
        InfoUsuario info = user.getInfo();

        // Retorna una nueva instancia de UsuarioDTO con todos los datos asignados
        return new UsuarioDTO(
            user.getNombre(),
            user.getApellido(),
            user.getNickname(),
            user.getCorreo(),
            user.getFotoPerfil(),
            user.getMonedas(),
            user.getRole().name(),  // Se convierte el enum Role a String

            // Se acceden a las redes sociales solo si 'info' no es null, para evitar NullPointerException
            info != null ? info.getDiscord() : null,
            info != null ? info.getYoutube() : null,
            info != null ? info.getTwitch() : null,
            info != null ? info.getKick() : null,

            user.getEquipo() // Asocia el equipo actual del usuario
        );
    }
}
