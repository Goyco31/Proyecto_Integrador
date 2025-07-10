package com.integrador.spring.app.Servicio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Random;
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

    @Value("${spring.mail.username}") // Inyecta el email del remitente desde application.properties
    private String fromEmail;

    // Método para enviar correo de bienvenida con plantilla personalizada
    public void sendWelcomeEmail(String toEmail, String nombre, String nickname) {
        try {
            MimeMessage message = mailSender.createMimeMessage(); // Crear correo MIME
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail); // Remitente
            helper.setTo(toEmail);     // Destinatario
            helper.setSubject("¡Bienvenido a nuestra plataforma!"); // Asunto del correo

            // Crear contexto con variables para la plantilla
            Context context = new Context();
            context.setVariable("nombre", nombre);
            context.setVariable("nickname", nickname);

            // Procesar plantilla HTML con Thymeleaf
            String htmlContent = templateEngine.process("welcome-email", context);
            helper.setText(htmlContent, true); // true = formato HTML

            mailSender.send(message); // Enviar correo
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo electrónico", e); // Manejo de errores
        }
    }

    // Método para enviar código de autenticación 2FA
    public void send2FACode(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Código de verificación - Two Factor Authentication");

            Context context = new Context();
            context.setVariable("code", code);             // Código de verificación
            context.setVariable("expirationMinutes", 5);   // Tiempo de expiración

            String htmlContent = templateEngine.process("email/2fa-code", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("Código 2FA enviado a: " + toEmail); // Confirmación en consola
        } catch (MessagingException e) {
            System.err.println("Error al enviar código 2FA: " + e.getMessage());
            throw new RuntimeException("Error al enviar código de verificación", e);
        }
    }

    // Método para enviar correo con instrucciones de configuración 2FA
    public void send2FASetupEmail(String toEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Configuración de Seguridad - Verificación en Dos Pasos");

            Context context = new Context(); // No se pasan variables en este caso

            String htmlContent = templateEngine.process("email/2fa-setup", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo de configuración 2FA", e);
        }
    }

    // Método para enviar correo con una recompensa canjeada
    public void recompensaEmail(String email, String nombreRecompensa, String recompensaDescripcion) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("¡Recompensa Canjeada!");

            // Lista de códigos para asignar aleatoriamente
            String[] codigos = {
                "3DJK9-GKCC6-FPRQQ-X4D2M-QR6HZ",
                "Q272C-K2H3C-RTQJ9-DYFT2-GH3JZ",
                "7R39G-HH77R-M74YJ-M9JM6-K364Z",
                "VCW37-T7QWV-292X3-DMC9H-2CPMZ",
                "CJ3HW-QVDFG-RTX2F-KCPJR-TPMFZ"
            };
            Random random = new Random();
            int numRandom = random.nextInt(codigos.length); // Índice aleatorio
            String codigoRandom = codigos[numRandom];       // Código elegido

            // Crear contexto para plantilla
            Context context = new Context();
            context.setVariable("nombre", nombreRecompensa);
            context.setVariable("descripcion", recompensaDescripcion);
            context.setVariable("codigo", codigoRandom);

            String htmlContent = templateEngine.process("email/recompensaCorreo", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo electrónico de recompensa", e);
        }
    }
}
