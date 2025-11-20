package com.chris.uniconnect.Service.Impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailServiceImpl {
    @Value("${SENDGRID_API_KEY}")
    private String sendgridApiKey;

    public void sendEmail(String toEmail, String subject, String contentText) throws Exception {

        Email from = new Email("centrouniversitariotianguisten@gmail.com");
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", contentText);

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
}
