package com.integrador.spring.app.Servicio;

// Importaciones de Spring Security y otras utilidades
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.Controlador.ControladorResponse;
import com.integrador.spring.app.Controlador.LoginRequest;
import com.integrador.spring.app.Controlador.RegisterRequest;
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
    private final EmailService emailService;

    //Método para autenticar (login) un usuario.
    public ControladorResponse login(LoginRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getNickname(),
                request.getContraseña()
            )
        );

        // Generar token JWT
        User user = userDAO.findByNickname(request.getNickname())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.getToken(user);

        return ControladorResponse.builder()
            .mensaje("Autenticación exitosa")
            .token(token)
            .build();
    }
    
    //Método para registrar un nuevo usuario.
    public ControladorResponse registro(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if(userDAO.findByNickname(request.getNickname()).isPresent()) {
            throw new RuntimeException("El nickname ya está en uso");
        }

        // Crear nuevo usuario
        User user = User.builder()
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .nickname(request.getNickname())
            .correo(request.getCorreo())
            .contraseña(passwordEncoder.encode(request.getContraseña()))
            .role(role.USER)
            .build();

        userDAO.save(user);

        try {
            emailService.sendWelcomeEmail(
                user.getCorreo(), 
                user.getNombre(),
                user.getNickname()
            );
        } catch (Exception e) {
            // Log the error but don't fail registration
            System.err.println("Error enviando correo: " + e.getMessage());
        }

        return ControladorResponse.builder()
            .mensaje("Usuario registrado exitosamente")
            .build();
    }

}
