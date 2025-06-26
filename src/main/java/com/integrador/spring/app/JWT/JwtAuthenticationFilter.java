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

//Filtro que intercepta cada solicitud HTTP una sola vez para validar el JWT
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserDAO userDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Skip JWT filter for registration and login endpoints
        if (request.getServletPath().startsWith("/control/registro") || 
            request.getServletPath().startsWith("/control/login") ||
            request.getServletPath().startsWith("/control/validate-2fa")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = getTokenFromRequest(request);
        
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String username = jwtService.getUsernameFromToken(token);
            System.out.println("TOKEN DECODIFICADO: " + username);
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                if (jwtService.isTokenValid(token, userDetails)) {
                    // Verificación 2FA solo para rutas protegidas
                    if (requires2FACheck(request) && !is2FAVerified(username)) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"Se requiere verificación 2FA\", \"message\": \"Complete la autenticación en dos pasos\"}");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException ex) {
            handleExpiredToken(request, response, ex);
            return;
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Autenticación fallida\", \"message\": \"" + e.getMessage() + "\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean requires2FACheck(HttpServletRequest request) {
        // Verifica si la ruta requiere 2FA (todas las rutas /api/ excepto algunas públicas)
        return request.getRequestURI().startsWith("/api/") &&
               !request.getRequestURI().startsWith("/api/public/");
    }

    private boolean is2FAVerified(String username) {
        User user = userDAO.findByNickname(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Usuarios sin 2FA activado se consideran verificados
        if (!user.is2faEnabled()) {
            return true;
        }
        
        // Verifica si el código 2FA fue usado (campo twoFactorCode == null)
        return user.getTwoFactorCode() == null;
    }



    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException ex) 
            throws IOException {
        // Verificar si es una solicitud de refresh token
        if (isRefreshTokenRequest(request)) {
            allowForRefreshToken(ex, request);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expirado\", \"message\": \"Por favor, renueve su token\"}");
        }
    }

    private boolean isRefreshTokenRequest(HttpServletRequest request) {
        String isRefreshToken = request.getHeader("isRefreshToken");
        String requestURL = request.getRequestURL().toString();
        return "true".equals(isRefreshToken) && requestURL.contains("refresh-token");
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            null, null, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        request.setAttribute("claims", ex.getClaims());
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}