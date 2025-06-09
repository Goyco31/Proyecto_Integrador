package com.integrador.spring.app.Servicio;

import java.util.List;

// Importaciones de Spring Security y otras utilidades
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.integrador.spring.app.Controlador.ControladorResponse;
import com.integrador.spring.app.Controlador.LoginRequest;
import com.integrador.spring.app.Controlador.RegisterRequest;
import com.integrador.spring.app.Controlador.Validate2FARequest;
// Importaciones de clases del proyecto
import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Modelo.role;

// Importación de Lombok para constructor con argumentos requeridos (final)
import lombok.RequiredArgsConstructor;

// Marca esta clase como un servicio de Spring
@Service
// Lombok genera automáticamente un constructor con los atributos marcados como final
@RequiredArgsConstructor
public class ControladorService {

    private static final Logger logger = LoggerFactory.getLogger(ControladorService.class);
    // Inyección de dependencias necesarias para autenticación, acceso a datos, y codificación de contraseñas
    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TwoFactorAuthService twoFactorAuthService;

    public List<List<User>> getUsersInBatches(int batchSize) {
        Preconditions.checkArgument(batchSize > 0, "El tamaño del lote debe ser mayor a 0");
        List<User> allUsers = userDAO.findAll();
        return Lists.partition(allUsers, batchSize);
    }
    
    //Método para autenticar (login) un usuario.
    public ControladorResponse login(LoginRequest request) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(request.getNickname()), "Nickname requerido");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(request.getContraseña()), "Contraseña requerida");
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
        try {
            // Normalizar y validar
            request.normalize();
            request.validate();

            // Verificar unicidad del nickname
            if (userDAO.findByNickname(request.getNickname()).isPresent()) {
                logger.warn("Intento de registro con nickname duplicado: {}", request.getNickname());
                throw new RuntimeException("El nickname ya está en uso");
            }

            // Determinar rol
            boolean isAdminEmail = StringUtils.endsWithIgnoreCase(request.getCorreo(), "@utp.edu.pe");

            // Crear usuario
            User user = User.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .nickname(request.getNickname())
                .correo(request.getCorreo())
                .contraseña(passwordEncoder.encode(request.getContraseña()))
                .role(isAdminEmail ? role.ADMIN : role.USER)
                .is2faEnabled(true)
                .build();

            userDAO.save(user);
            logger.info("Nuevo usuario registrado: {}", user.getNickname());

            // Enviar correos
            emailService.sendWelcomeEmail(user.getCorreo(), user.getNombre(), user.getNickname());
            emailService.send2FASetupEmail(user.getCorreo());

            return ControladorResponse.builder()
                .mensaje("Usuario registrado exitosamente como " + (isAdminEmail ? "ADMIN" : "USER"))
                .build();

        } catch (Exception e) {
            logger.error("Error en registro: " + e.getMessage(), e);
            throw e; // Re-lanzar para manejo en el controlador
        }
    }


    public String toggle2FA(String nickname) {
        User user = userDAO.findByNickname(nickname)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.set2faEnabled(!user.is2faEnabled());
        userDAO.save(user);
        
        return "2FA " + (user.is2faEnabled() ? "activado" : "desactivado");
    }

    public ControladorResponse validate2FA(Validate2FARequest request) {
        boolean isValid = twoFactorAuthService.verify2FACode(request.getEmail(), request.getTwoFactorCode());
        if (!isValid) {
            throw new RuntimeException("Código 2FA inválido");
        }
        
        User user = userDAO.findByCorreo(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        String token = jwtService.getToken(user);
        
        // Limpiar código 2FA
        user.setTwoFactorCode(null);
        user.setTwoFactorExpiry(null);
        userDAO.save(user);
        
        return ControladorResponse.builder()
            .token(token)
            .mensaje("Autenticación exitosa")
            .build();
    }
}
