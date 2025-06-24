package com.integrador.spring.app.Servicio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}") // Inyecta el email desde properties
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String nombre, String nickname) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("¡Bienvenido a nuestra plataforma!");
            
            // Contexto para la plantilla
            Context context = new Context();
            context.setVariable("nombre", nombre);
            context.setVariable("nickname", nickname);
            
            // Procesar plantilla HTML
            String htmlContent = templateEngine.process("welcome-email", context);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo electrónico", e);
        }
    }

    // Método para enviar códigos 2FA
    public void send2FACode(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Código de verificación - Two Factor Authentication");
            
            // Contexto para la plantilla
            Context context = new Context();
            context.setVariable("code", code);
            context.setVariable("expirationMinutes", 5); // Tiempo de expiración
            
            // Procesar plantilla HTML
            String htmlContent = templateEngine.process("email/2fa-code", context);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            System.out.println("Código 2FA enviado a: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Error al enviar código 2FA: " + e.getMessage());
            throw new RuntimeException("Error al enviar código de verificación", e);
        }
    }
    public void send2FASetupEmail(String toEmail) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Configuración de Seguridad - Verificación en Dos Pasos");
        
        Context context = new Context();
        String htmlContent = templateEngine.process("email/2fa-setup", context);
        helper.setText(htmlContent, true);
        
        mailSender.send(message);
    } catch (MessagingException e) {
        throw new RuntimeException("Error al enviar correo de configuración 2FA", e);
    }
}

    public void recompensaEmail(String email, String nombreRecompensa, String recompensaDescripcion) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("¡Recompensa Canjeada!");

            Context context = new Context();
            context.setVariable("nombre", nombreRecompensa);
            context.setVariable("descripcion", recompensaDescripcion);

            String htmlContent = templateEngine.process("email/recompensaCorreo", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo electrónico de recompensa", e);
        }
    }
}
