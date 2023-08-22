package com.kino_cms.sendMailer;

import com.kino_cms.dto.UserDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;


@Service
public class MailSenderService {
    @Value("${upload.path}")
    private String uploadPath;
    private static final String SUBJECT = "Kino CMS Spring Application";
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toMail,
                          String subject,
                          String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@gmail.com");
        message.setTo(toMail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendTemplate(String toMail,
                             String templateName) {
        File template = new File(uploadPath + "/html_templates/" + templateName);
        StringBuilder body = new StringBuilder();
        try {
            Scanner scanner = new Scanner(template);
            while (scanner.hasNext()) {
                body.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom("noreply@gmail.com");
            message.setContent(body.toString(), "text/html");
            helper.setTo(toMail);
            helper.setSubject(SUBJECT);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    public void sendAll(List<UserDTO> users) {
        for (UserDTO user : users) {
            String mail = user.getEmail();
            String name = user.getFirstName() + " " + user.getLastName();
            String body = "Hi, dear " + name + " nice to meet you :)";

            sendEmail(mail, " new feed", body);
        }
    }

}
