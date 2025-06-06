package com.integrador.spring.app.Controlador;



import java.util.List;

// Importaciones necesarias para manejar respuestas HTTP y anotaciones de Spring
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.ControladorService;
import com.integrador.spring.app.Servicio.JwtService;
import com.integrador.spring.app.Servicio.TorneoService;
import com.integrador.spring.app.Servicio.TwoFactorAuthService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final TorneoService torneoService; // Inyectar el servicio de torneos
    private final TwoFactorAuthService twoFactorAuthService;

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

    @PostMapping("/verify-2fa")
    public ResponseEntity<ControladorResponse> verify2FA(
            @RequestParam String code,
            HttpServletRequest request) {
        
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ControladorResponse.builder()
                        .mensaje("Token de autorización faltante")
                        .build());
            }

            String tempToken = authHeader.substring(7);
            String username = jwtService.getUsernameFromToken(tempToken);
            User user = (User) userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTempTokenValid(tempToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ControladorResponse.builder()
                        .mensaje("Token temporal expirado")
                        .build());
            }

            if (!twoFactorAuthService.verify2FACode(user.getCorreo(), code)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ControladorResponse.builder()
                        .mensaje("Código 2FA inválido")
                        .build());
            }

            String finalToken = jwtService.getTokenWith2FAVerified(user);
            
            return ResponseEntity.ok(ControladorResponse.builder()
                .mensaje("Autenticación exitosa")
                .token(finalToken)
                .build());
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ControladorResponse.builder()
                    .mensaje("Error en la verificación: " + e.getMessage())
                    .build());
        }
    }

    @PostMapping("/torneos")
    @ResponseBody
    public ResponseEntity<?> crearTorneo(
            @RequestBody @Valid TorneoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {  // Eliminamos HttpServletRequest ya que no es necesario
        
        try {
            // Verificación 2FA usando el username del UserDetails
            if (!jwtService.is2FAComplete(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Debes completar la autenticación en dos pasos");
            }

            // Verificación de permisos (opcional, si decides mantenerlo)
            boolean canCreateTournament = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || 
                            a.getAuthority().equals("ROLE_USER"));  // Ajusta según tus necesidades
            
            if (!canCreateTournament) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permisos para crear torneos");
            }

            // Creación del torneo
            Torneo torneoCreado = torneoService.crearTorneo(request, userDetails.getUsername());
            return ResponseEntity.ok(torneoCreado);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear torneo: " + e.getLocalizedMessage());
        }
    }
    
    @GetMapping("/torneos")
    @ResponseBody
    public ResponseEntity<List<Torneo>> listarTorneos() {
        return ResponseEntity.ok(torneoService.listarTodosLosTorneos());
    }

    @GetMapping("/torneos/{id}")
    @ResponseBody
    public ResponseEntity<Torneo> obtenerTorneo(@PathVariable Integer id) {
        return torneoService.obtenerTorneoPorId(id)
                           .map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/torneos/{id}")
    @ResponseBody
    public ResponseEntity<Torneo> actualizarTorneo(
            @PathVariable Integer id, 
            @RequestBody Torneo torneo) {
        return ResponseEntity.ok(torneoService.actualizarTorneo(id, torneo));
    }

    @DeleteMapping("/torneos/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarTorneo(@PathVariable Integer id) {
        torneoService.eliminarTorneo(id);
        return ResponseEntity.noContent().build();
    }
        
  
}