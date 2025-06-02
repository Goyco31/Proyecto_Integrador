package com.integrador.spring.app.JWT;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Skip JWT filter for registration and login endpoints
        if (request.getServletPath().startsWith("/control/registro") || 
            request.getServletPath().startsWith("/control/login")) {
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
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException ex) {
            handleExpiredToken(request, response, ex);
            return;
        }
        
        filterChain.doFilter(request, response);
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