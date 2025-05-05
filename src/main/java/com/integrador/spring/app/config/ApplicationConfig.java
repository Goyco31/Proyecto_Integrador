package com.integrador.spring.app.config;

// Importaciones necesarias para configurar seguridad y autenticación
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// DAO para acceder a los datos del usuario
import com.integrador.spring.app.DAO.UserDAO;

// Anotación para generar constructor con todos los campos finales
import lombok.RequiredArgsConstructor;


// Clase de configuración que define los beans relacionados con seguridad
@Configuration
@RequiredArgsConstructor// Genera constructor para inyectar userDAO automáticamente
public class ApplicationConfig {

    // DAO que permite buscar usuarios en la base de datos
    private final UserDAO userDAO;

    // Bean que expone el AuthenticationManager necesario para autenticación en Spring Security
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    // Bean que define el proveedor de autenticación usando DAO y BCrypt
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    // Bean que define el codificador de contraseñas usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean que define el servicio para cargar usuarios por su nickname
    @Bean
    public UserDetailsService userDetailService() {
        return nickname ->userDAO.findByNickname(nickname)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
