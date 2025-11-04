package org.emmanuel.chewallet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // URL base del front-end (puedes moverla a application.properties si quieres)
    private static final String FRONTEND_URL = "https://che-wallet-git-comprobante-test-mariano-ocaranzas-projects.vercel.app";

    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = FRONTEND_URL + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Recuperación de contraseña - Che Wallet");
        message.setText("""
                Hola,

                Has solicitado restablecer tu contraseña en Che Wallet.
                Por favor, haz clic en el siguiente enlace para crear una nueva contraseña:

                %s

                ⚠️ Este enlace solo puede usarse una vez y expirará en 15 minutos.

                Si no solicitaste este cambio, simplemente ignora este mensaje.

                Saludos,
                El equipo de Che Wallet.
                """.formatted(resetLink));

        mailSender.send(message);
    }
}
