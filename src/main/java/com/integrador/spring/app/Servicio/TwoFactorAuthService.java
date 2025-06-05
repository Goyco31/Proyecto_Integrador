package com.integrador.spring.app.Servicio;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthService {
    private final UserDAO userDao;
    private final EmailService emailService;

    public String generateAndSend2FACode(String email) {
        User user = userDao.findByCorreo(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar código de 6 dígitos
        String code = String.format("%06d", new Random().nextInt(999999));
        
        // Establecer expiración (5 minutos en el futuro)
        user.setTwoFactorCode(code);
        user.setTwoFactorExpiry(LocalDateTime.now().plusMinutes(5));
        userDao.save(user);

        // Enviar por correo
        emailService.send2FACode(email, code);

        return code;
    }

    public boolean verify2FACode(String email, String code) {
        // DEBUG
        System.out.println("Verificando código para email: " + email);
        
        User user = userDao.findByCorreo(email)
            .orElseThrow(() -> {
                System.err.println("Usuario no encontrado con email: " + email);
                return new RuntimeException("Usuario no encontrado");
            });

        boolean isValid = code.equals(user.getTwoFactorCode()) && 
            LocalDateTime.now().isBefore(user.getTwoFactorExpiry());
        
        System.out.println("Código válido: " + isValid);
        return isValid;
    }
}
