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
    private final TwoFactorAuthService twoFactorAuthService;
    private final UserDAO userDao;
    
    //Método para autenticar (login) un usuario.
    public ControladorResponse login(LoginRequest request) {
        // 1. Autenticación básica
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getNickname(),
                request.getContraseña()
            )
        );

        User user = userDAO.findByNickname(request.getNickname())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // DEBUG: Verifica datos del usuario
        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Nickname: " + user.getNickname());
        System.out.println("Email: " + user.getCorreo());
        System.out.println("2FA Enabled: " + user.is2faEnabled());

        // 2. Verificar si requiere 2FA
        if (user.is2faEnabled()) {
            // Si ya viene con código, verificar
            if (request.getTokenVerificacion() != null) {
                if (twoFactorAuthService.verify2FACode(user.getCorreo(), request.getTokenVerificacion())) {
                    String token = jwtService.getToken(user);
                    return ControladorResponse.builder()
                        .mensaje("Autenticación exitosa")
                        .token(token)
                        .build();
                }
                throw new RuntimeException("Código 2FA inválido o expirado");
            }
            
            // Enviar código al EMAIL (corregido)
            twoFactorAuthService.generateAndSend2FACode(user.getCorreo()); // Cambiado de nickname a correo
            
            String tempToken = jwtService.generateTempToken(user);
            
            return ControladorResponse.builder()
                .mensaje("Se requiere verificación en dos pasos. Código enviado al correo.")
                .requires2fa(true)
                .tempToken(tempToken)
                .build();
        }

        // 3. Si no requiere 2FA
        String token = jwtService.getToken(user);
        return ControladorResponse.builder()
            .mensaje("Autenticación exitosa")
            .token(token)
            .build();
    }

    
    public ControladorResponse registro(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if(userDAO.findByNickname(request.getNickname()).isPresent()) {
            throw new RuntimeException("El nickname ya está en uso");
        }

        // Crear nuevo usuario con 2FA activado por defecto
        User user = User.builder()
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .nickname(request.getNickname())
            .correo(request.getCorreo())
            .contraseña(passwordEncoder.encode(request.getContraseña()))
            .role(role.USER)
            .is2faEnabled(true) // 2FA activado automáticamente
            .build();

        userDAO.save(user);

        try {
            emailService.sendWelcomeEmail(
                user.getCorreo(), 
                user.getNombre(),
                user.getNickname()
            );
            
            // Opcional: Enviar instrucciones sobre el 2FA
            emailService.send2FASetupEmail(user.getCorreo());
            
        } catch (Exception e) {
            System.err.println("Error enviando correo: " + e.getMessage());
        }

        return ControladorResponse.builder()
            .mensaje("Usuario registrado exitosamente. Se ha activado la verificación en dos pasos.")
            .build();
    }


    public String toggle2FA(String nickname) {
        User user = userDao.findByNickname(nickname)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.set2faEnabled(!user.is2faEnabled());
        userDao.save(user);
        
        return "2FA " + (user.is2faEnabled() ? "activado" : "desactivado");
    }
}
