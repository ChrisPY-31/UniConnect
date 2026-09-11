package com.chris.uniconnect.Service.Impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;

@Service
public class EmailServiceImpl {

    private static final String FROM_EMAIL = "centrouniversitariotianguisten@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(String toEmail, String subject, String username, String password) throws Exception {

        // 1. Cargar template desde resources/templates
        String html = new String(
                getClass().getClassLoader().getResourceAsStream("templates/email-template.html")
                        .readAllBytes()
        );

        // 2. Reemplazar variables dinámicas
        html = html.replace("{{username}}", username);
        html = html.replace("{{password}}", password);

        sendHtml(toEmail, subject, html);
    }

    public void sendEmailUpdate(String toEmail, String subject, String htmlContent) throws Exception {
        sendHtml(toEmail, subject, htmlContent);
    }

    private void sendHtml(String toEmail, String subject, String htmlContent) throws Exception {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(FROM_EMAIL);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new Exception("Error enviando correo: " + e.getMessage(), e);
        }
    }

    public String loadTemplate(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource("templates/" + fileName);
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }
}
