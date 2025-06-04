package com.integrador.spring.app.Controlador;



// Importaciones necesarias para manejar respuestas HTTP y anotaciones de Spring
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.integrador.spring.app.Servicio.ControladorService;
import com.integrador.spring.app.Servicio.JwtService;

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
        
  
}