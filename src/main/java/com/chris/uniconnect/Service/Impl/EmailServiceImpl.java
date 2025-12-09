package com.chris.uniconnect.Service.Impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.apache.tomcat.util.security.Escape;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl {
    @Value("${SENDGRID_API_KEY}")
    private String sendgridApiKey;

    public void sendHtmlEmail(String toEmail, String subject, String username, String password) throws Exception {

        // 1. Cargar template desde resources/templates
        String html = new String(
                getClass().getClassLoader().getResourceAsStream("templates/email-template.html")
                        .readAllBytes()
        );

        // 2. Reemplazar variables dinámicas
        html = html.replace("{{username}}", username);
        html = html.replace("{{password}}", password);


        // 3. Crear contenido HTML
        Content content = new Content("text/html", html);

        Email from = new Email("centrouniversitariotianguisten@gmail.com");
        Email to = new Email(toEmail);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (Exception e) {
            throw new Exception("Error enviando correo: " + e.getMessage(), e);
        }
    }

    public void sendEmailUpdate(String toEmail, String subject, String htmlContent) throws Exception {

        Email from = new Email("centrouniversitariotianguisten@gmail.com");
        Email to = new Email(toEmail);
        Content content = new Content("text/html", htmlContent);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (Exception e) {
            throw new Exception("Error enviando correo: " + e.getMessage(), e);
        }
    }

    public String loadTemplate(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource("templates/" + fileName);
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }
}
