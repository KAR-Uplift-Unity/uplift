package com.KARUpliftUnity.services;

import com.KARUpliftUnity.models.ContactForm;
import com.KARUpliftUnity.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;

    public void sendContactFormEmail(ContactForm form) {
        String subject = "Contact Us Form Submission";

        // Construct the email body
        String body = "Name: " + form.getName() +
                "\nEmail: " + form.getEmail() +
                "\nMessage: " + form.getMessage();

        sendEmail("email@email.com", subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);

        try {
            this.emailSender.send(msg);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void prepareAndSend(Post post, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(post.getUser().getEmail());
        msg.setSubject(subject);
        msg.setText(body);

        try{
            this.emailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
}

