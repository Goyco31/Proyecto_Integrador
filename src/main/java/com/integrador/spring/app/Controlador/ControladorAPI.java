package com.integrador.spring.app.Controlador;



import java.util.Map;
import java.util.Optional;

// Importaciones necesarias para manejar respuestas HTTP y anotaciones de Spring
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.DTO.LoginRequest;
import com.integrador.spring.app.DTO.RegisterRequest;
import com.integrador.spring.app.DTO.Validate2FARequest;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.ControladorService;
import com.integrador.spring.app.Servicio.JwtService;
import com.integrador.spring.app.Servicio.TwoFactorAuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



// Anotación que indica que esta clase es un controlador REST de Spring
@Controller
// Define la ruta base para todas las solicitudes que maneja este controlador
@RequestMapping("/control")
// Genera un constructor con los atributos requeridos (final), útil para inyección de dependencias
@RequiredArgsConstructor
public class ControladorAPI {
    // Servicio que maneja la lógica de autenticación y registro
    private final ControladorService authService;
    private final JwtService jwtService; // Inyectar JwtService
    private final UserDetailsService userDetailsService; // Inyectar UserDetailsService
    private final UserDAO userDAO;
    private final TwoFactorAuthService twoFactorAuthService;
    private final PasswordEncoder passwordEncoder;

    // Recibe los datos de login en el cuerpo de la solicitud y los pasa al servicio
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ControladorResponse> login(@RequestBody LoginRequest request) {
        // Devuelve la respuesta del servicio envuelta en un ResponseEntity con estado HTTP 200 OK
        return ResponseEntity.ok(authService.login(request));
    }

    // Recibe los datos de registro en el cuerpo de la solicitud y los pasa al servicio(para el manejo de solicitud post a la ruta: "/control/registro")
    @PostMapping("/registro")
    @ResponseBody
    public ResponseEntity<ControladorResponse> registro(@RequestBody RegisterRequest request) {
        // Devuelve la respuesta del servicio envuelta en un ResponseEntity con estado HTTP 200 OK
        return ResponseEntity.ok(authService.registro(request));
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ControladorResponse> refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token faltante");
        }
        
        String refreshToken = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(refreshToken);
        
        if (username != null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(refreshToken, user)) {
                String newAccessToken = jwtService.getToken(user);
                
                return ResponseEntity.ok(ControladorResponse.builder()
                    .mensaje("Token refrescado")
                    .token(newAccessToken)
                    .build());
            }
        }
        
        throw new RuntimeException("Refresh token inválido");
    }

    @PostMapping("/toggle-2fa")
    @ResponseBody
    public ResponseEntity<String> toggle2FA(@RequestParam String nickname) {
        return ResponseEntity.ok(authService.toggle2FA(nickname));
    }
     
    @PostMapping("/validate-2fa")
    public ResponseEntity<ControladorResponse> validate2FA(@RequestBody Validate2FARequest request) {
        return ResponseEntity.ok(authService.validate2FA(request));
    }
    

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        Optional<User> userOpt = userDAO.findByCorreo(correo);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Correo no encontrado"));
        }

        // Generar código (puedes usar el servicio que ya tienes)
        twoFactorAuthService.generateAndSend2FACode(correo);

        return ResponseEntity.ok(Map.of("message", "Código enviado al correo"));
    }

    @PostMapping("/validate-reset-code")
    public ResponseEntity<?> validateResetCode(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String code = body.get("code");

        Optional<User> userOpt = userDAO.findByCorreo(correo);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }

        boolean valido = twoFactorAuthService.verify2FACode(correo, code);
        if (!valido) {
            return ResponseEntity.status(400).body(Map.of("message", "Código incorrecto o expirado"));
        }

        return ResponseEntity.ok(Map.of("message", "Código verificado"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String nueva = body.get("nueva");

        Optional<User> userOpt = userDAO.findByCorreo(correo);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado"));
        }

        User user = userOpt.get();
        user.setContraseña(passwordEncoder.encode(nueva));
        user.setTwoFactorCode(null); // Limpieza si es que se reutilizó el campo
        user.setTwoFactorExpiry(null);
        userDAO.save(user);

        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }


}