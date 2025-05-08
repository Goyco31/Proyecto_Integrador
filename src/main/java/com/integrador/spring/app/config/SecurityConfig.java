package com.integrador.spring.app.config;

// Importaciones necesarias para configurar seguridad HTTP, sesiones y filtros
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Filtro personalizado para autenticación JWT
import com.integrador.spring.app.JWT.JwtAuthenticationFilter;

// Para la inyección de dependencias por constructor
import lombok.RequiredArgsConstructor;

// Indica que esta clase define una configuración
@Configuration
// Habilita la seguridad web de Spring
@EnableWebSecurity
// Crea automáticamente un constructor con los campos finales requeridos
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro JWT personalizado que se ejecutará antes del filtro de autenticación por usuario y contraseña
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // Proveedor de autenticación que se usará para verificar credenciales
    private final AuthenticationProvider authProvider;


    // Bean que configura la cadena de filtros de seguridad para las peticiones HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        
        return http
            .csrf(csrf ->
                csrf
                .disable()) // Desactiva CSRF ya que se usa JWT (no sesiones)
            .authorizeHttpRequests(authRequest ->
                authRequest
                    // Recursos públicos y estáticos
                    .requestMatchers("/", "/index", "/css/**", "/js/**", "/images/**",
                    "/Explorar", "/Clasificacion", "/usuario", "/skill",
                    "/control/login", "/control/registro").permitAll()
                    // Todo lo demás, incluyendo /control/** excepto login y registro, requiere autenticación
                    .anyRequest().authenticated()
            )
        .sessionManagement(sessionManager -> 
            // Configura la política de sesión como STATELESS (sin sesiones, solo JWT)
            sessionManager
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Establece el proveedor de autenticación definido previamente
                .authenticationProvider(authProvider)
                // Añade el filtro JWT antes del filtro estándar de autenticación
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Construye y retorna la cadena de seguridad
                .build();
    }

    
}
