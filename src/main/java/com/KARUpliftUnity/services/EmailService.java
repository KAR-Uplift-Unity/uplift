package com.KARUpliftUnity.services;

import com.KARUpliftUnity.models.ContactForm;
import com.KARUpliftUnity.models.Post;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service("emailService")
public class EmailService {


    public final JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String from;
    @Autowired
    public EmailService(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }


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

    public void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            mimeMessage.saveChanges();
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            String error = e.toString();
            System.out.println("error = " + error);
        }
    }
}

