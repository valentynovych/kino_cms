package com.kino_cms.service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceTest {

    private MailSenderService mailSenderService;
    private JavaMailSender mailSender;
    private File template;

    @BeforeEach
    void init() {
        mailSender = mock(JavaMailSender.class);
        mailSenderService = new MailSenderService(mailSender);
        template = mock(File.class);
    }

    @Test
    void sendEmail_withFile() {
        ReflectionTestUtils.setField(mailSenderService, "uploadPath", "");
        MimeMessage mMessage = new MimeMessage(Session.getInstance(new Properties()));

        when(mailSender.createMimeMessage()).thenReturn(mMessage);

        String to = "mail@mail.com";
        String templateName = "template.html";
        mailSenderService.sendEmail(to, templateName);
        Mockito.verify(mailSender).createMimeMessage();

//        template = new File("/html_templates/template.html");
        when(template.exists()).thenReturn(Boolean.TRUE);
        when(template.getName()).thenReturn(templateName);

        if (!template.exists()) {
            try (FileOutputStream fileWriter = new FileOutputStream(template)) {
                fileWriter.write("string\nstring".getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            mailSenderService.sendEmail(to, template.getName());
        }
        if (template.exists()) {
            if (template.delete()) {
                System.out.println("file deleted");
            }
        }

        mailSenderService.sendEmail(null, templateName);


    }
}