package com.integrador.spring.app.Controlador;

// Importaciones de Spring Security y otras utilidades
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Importaciones de clases del proyecto
import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.JWT.JwtService;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Modelo.role;

// Importación de Lombok para constructor con argumentos requeridos (final)
import lombok.RequiredArgsConstructor;

// Marca esta clase como un servicio de Spring
@Service
// Lombok genera automáticamente un constructor con los atributos marcados como final
@RequiredArgsConstructor
public class ControladorService {

    // Inyección de dependencias necesarias para autenticación, acceso a datos, y codificación de contraseñas
    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    //Método para autenticar (login) un usuario.
    public ControladorResponse login(LoginRequest request) {
        // Autentica el usuario usando Spring Security (verifica nickname y contraseña)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getNickname(), request.getContraseña()));
        UserDetails user = userDAO.findByNickname(request.getNickname()).orElseThrow();
        String token=jwtService.getToken(user);
        return ControladorResponse.builder()
            .token(token)
            .build();
    }
    
    //Método para registrar un nuevo usuario.
    public ControladorResponse registro(RegisterRequest request) {
        // Crea un nuevo objeto de usuario con los datos del formulario, codificando la contraseña
        User user = User.builder()
            .nombre(request.getNombre())
            .contraseña(passwordEncoder.encode(request.getContraseña()))
            .apellido(request.getApellido())
            .nickname(request.getNickname())
            .correo(request.getCorreo())
            .role(role.USER)
            .build();

            userDAO.save(user);

            return ControladorResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

}
