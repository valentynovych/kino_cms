package com.kino_cms.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


@Service
@RequiredArgsConstructor
@Log4j2
public class MailSenderService {
    @Value("${upload.path}")
    private String uploadPath;
    private final JavaMailSender mailSender;
    private static final String SUBJECT = "Kino CMS Spring Application";

    public void sendEmail(String to, String templateName) {
        log.info(String.format("-> start execution method sendEmail(to %s, templateName %s)", to, templateName));

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom("noreply@gmail.com");
            if (to != null) {
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            } else {
                throw new MessagingException();
            }
            message.setSubject(SUBJECT);
            String content = convertHtmlToString(templateName);
            message.setText(content, "UTF-8");

            mailSender.send(message);
        } catch (MessagingException e) {
            log.info("-> ERROR on method sendEmail(), errorMessage:" + e.getMessage());
        }
        log.info("-> exit from method sendEmail()");
    }

    private String convertHtmlToString(String filename) {
        log.info(String.format("-> start execution method convertHtmlToString(filename {%s})", filename));
        File file = new File(uploadPath + "/html_templates/" + filename);
        Scanner scanner;
        StringBuilder string = new StringBuilder();

        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                string.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            log.info("-> ERROR on method convertHtmlToString(), errorMessage:" + e.getMessage());
        }
        log.info("-> exit from method convertHtmlToString()");
        return string.toString();
    }
}
