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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//Filtro que intercepta cada solicitud HTTP una sola vez para validar el JWT
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    // Servicio para operaciones con tokens JWT
    private final JwtService jwtService;
    // Servicio para cargar los detalles del usuario autenticado
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Extrae el token JWT desde la cabecera Authorization
        final String token = getTokenFromRequest(request);
        // Si no hay token, continúa con la cadena de filtros sin hacer nada
        final String nickname;
        if(token==null){
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el nombre de usuario (nickname) desde el token
        nickname=jwtService.getUsernameFromToken(token);

        // Verifica si el usuario aún no está autenticado en el contexto de seguridad
        if(nickname!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            // Carga los detalles del usuario
            UserDetails userDetails=userDetailsService.loadUserByUsername(nickname);
            // Verifica si el token es válido con respecto al usuario
            if(jwtService.isTokenValid(token, userDetails)){
                // Crea el token de autenticación de Spring con los detalles del usuario
                UsernamePasswordAuthenticationToken contrToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                // Establece los detalles de la solicitud en el token
                contrToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Coloca el token de autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(contrToken);
            }
        }
        // Continúa con el resto de la cadena de filtros
        filterChain.doFilter(request, response);
    }
    
    //Extrae el token JWT del encabezado Authorization de la solicitud
    private String getTokenFromRequest(HttpServletRequest request){
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        // Verifica que el header no sea vacío y comience con "Bearer"
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")){
            return authHeader.substring(7);// Devuelve el token sin "Bearer "
        }
        return null;
    }

    
    
}
