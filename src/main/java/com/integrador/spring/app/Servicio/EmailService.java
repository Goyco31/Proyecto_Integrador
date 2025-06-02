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
}
