package com.integrador.spring.app.JWT;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Filtro personalizado que se ejecuta una sola vez por cada solicitud HTTP para validar el JWT
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Servicio para manejar tokens JWT (generación, validación, extracción de claims)
    private final JwtService jwtService;

    // Carga los detalles del usuario desde la base de datos
    private final UserDetailsService userDetailsService;

    // DAO para acceder a los datos del usuario desde la base de datos
    private final UserDAO userDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Si la solicitud es para registrar, loguear o validar 2FA, omitir validación JWT
        if (request.getServletPath().startsWith("/control/registro") || 
            request.getServletPath().startsWith("/control/login") ||
            request.getServletPath().startsWith("/control/validate-2fa")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el token JWT del encabezado Authorization
        final String token = getTokenFromRequest(request);
        
        // Si no hay token, continúa con el siguiente filtro
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Obtiene el username (nickname) del token
            final String username = jwtService.getUsernameFromToken(token);
            System.out.println("TOKEN DECODIFICADO: " + username);
            
            // Si hay un usuario válido y no hay autenticación previa en el contexto de seguridad
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carga los detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Verifica si el token es válido
                if (jwtService.isTokenValid(token, userDetails)) {
                    
                    // Si la ruta requiere 2FA y el usuario no ha verificado 2FA, bloquea la solicitud
                    if (requires2FACheck(request) && !is2FAVerified(username)) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"Se requiere verificación 2FA\", \"message\": \"Complete la autenticación en dos pasos\"}");
                        return;
                    }

                    // Crea el objeto de autenticación con los detalles del usuario y lo registra en el contexto
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException ex) {
            // Maneja casos de token expirado (por ejemplo, permite usar refresh token si aplica)
            handleExpiredToken(request, response, ex);
            return;
        } catch (Exception e) {
            // Cualquier error en la autenticación devuelve error 401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Autenticación fallida\", \"message\": \"" + e.getMessage() + "\"}");
            return;
        }
        
        // Continúa con el siguiente filtro de la cadena
        filterChain.doFilter(request, response);
    }

    // Determina si la ruta actual requiere validación de 2FA
    private boolean requires2FACheck(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/") &&
               !request.getRequestURI().startsWith("/api/public/");
    }

    // Verifica si el usuario ha pasado el proceso de verificación 2FA
    private boolean is2FAVerified(String username) {
        User user = userDAO.findByNickname(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Si el usuario no tiene 2FA activado, se considera verificado
        if (!user.is2faEnabled()) {
            return true;
        }

        // Si el código 2FA ya fue validado (es null), el usuario está verificado
        return user.getTwoFactorCode() == null;
    }

    // Maneja el caso cuando el token ha expirado
    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException ex) 
            throws IOException {
        
        // Si la solicitud es para renovar el token, permite continuar
        if (isRefreshTokenRequest(request)) {
            allowForRefreshToken(ex, request);
        } else {
            // Si no, devuelve error de token expirado
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expirado\", \"message\": \"Por favor, renueve su token\"}");
        }
    }

    // Verifica si la solicitud es para refresh token
    private boolean isRefreshTokenRequest(HttpServletRequest request) {
        String isRefreshToken = request.getHeader("isRefreshToken");
        String requestURL = request.getRequestURL().toString();
        return "true".equals(isRefreshToken) && requestURL.contains("refresh-token");
    }

    // Permite continuar la solicitud si se trata de un refresh token válido
    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            null, null, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        request.setAttribute("claims", ex.getClaims());
    }

    // Extrae el token JWT del encabezado Authorization (Bearer token)
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Quita "Bearer " para obtener solo el token
        }
        return null;
    }
}
