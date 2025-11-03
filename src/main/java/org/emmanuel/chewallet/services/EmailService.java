package org.emmanuel.chewallet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String link) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Recuperación de contraseña");
    message.setText("Haz click en el siguiente enlace para restablecer tu contraseña: " + link
            + "\nEste enlace expirará en 15 minutos.");
    mailSender.send(message);
    }
}
