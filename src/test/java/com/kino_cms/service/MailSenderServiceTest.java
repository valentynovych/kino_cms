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

    @BeforeEach
    void init() {
        mailSender = mock(JavaMailSender.class);
        mailSenderService = new MailSenderService(mailSender);
    }

    @Test
    void sendEmail_withFile() {
        ReflectionTestUtils.setField(mailSenderService, "uploadPath", "/D:/SpaceLab/kino_cms/uploads");
        MimeMessage mMessage = new MimeMessage(Session.getInstance(new Properties()));
        // MOCKS
        when(mailSender.createMimeMessage()).thenReturn(mMessage);

        String to = "mail@mail.com";
        String templateName = "template.html";
        mailSenderService.sendEmail(to, templateName);
        Mockito.verify(mailSender).createMimeMessage();

        File file = new File(System.getProperty("user.dir") + "/uploads/html_templates/template.html");

        if (!file.exists()) {
            try (FileOutputStream fileWriter = new FileOutputStream(file)) {
                fileWriter.write("string\nstring".getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mailSenderService.sendEmail(to, file.getName());
        }

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("file deleted");
            }
        }

        mailSenderService.sendEmail(null, templateName);


    }
}