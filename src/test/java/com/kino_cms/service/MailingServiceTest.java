package com.kino_cms.service;

import com.kino_cms.dto.MailingDTO;
import com.kino_cms.entity.MailingEntity;
import com.kino_cms.repository.MailingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailingServiceTest {
    @Mock
    MailingRepository mailingRepository;
    private MailingService mailingService;
    private MailingEntity mailingEntity;

    @BeforeEach
    void init() {
        mailingService = new MailingService(mailingRepository);

        mailingEntity = new MailingEntity();
        mailingEntity.setId(1L);
        mailingEntity.setCountMails(100);
        mailingEntity.setLastTemplates("template1.html, template2.html, " +
                "template3.html, template4.html, template5.html");
        mailingEntity.setLastUsedTemplate("template1.html");
    }

    @Test
    void getMailingEntity() {

        when(mailingRepository.findById(1L)).thenReturn(Optional.of(mailingEntity));
        MailingDTO mailingDTO = mailingService.getMailingEntity();
        assertEquals(mailingEntity.getCountMails(), mailingDTO.getCountMails());
        assertEquals(mailingEntity.getLastUsedTemplate(), mailingDTO.getLastUsedTemplate());
        List<String> stringList = mailingDTO.getLastTemplates();
        for (String temp : stringList) {
            assertTrue(mailingEntity.getLastTemplates().contains(temp));
        }

        when(mailingRepository.findById(1L)).thenReturn(Optional.empty());
        MailingDTO mailingDTO1 = mailingService.getMailingEntity();
        assertEquals(0, mailingDTO1.getCountMails());
        assertEquals("", mailingDTO1.getLastUsedTemplate());


    }

    @Test
    void saveMailingEntity() {

        when(mailingRepository.findById(1L)).thenReturn(Optional.of(mailingEntity));
        when(mailingRepository.save(mailingEntity)).thenReturn(mailingEntity);

        MailingDTO mailingDTO = new MailingDTO();
        mailingDTO.setCountMails(100);
        mailingDTO.setLastTemplates(
                List.of("template1.html", "template2.html",
                        "template3.html", "template4.html", "template5.html"));
        mailingDTO.setLastUsedTemplate("template1.html");

        MailingEntity entity = mailingService.saveMailingEntity(mailingDTO);

        assertEquals(mailingDTO.getCountMails(), entity.getCountMails());
        assertEquals(mailingDTO.getLastUsedTemplate(), entity.getLastUsedTemplate());
        List<String> stringList = mailingDTO.getLastTemplates();
        for (String temp : stringList) {
            assertTrue(entity.getLastTemplates().contains(temp));
        }

        when(mailingRepository.findById(1L)).thenReturn(Optional.empty());
        when(mailingRepository.save(mailingEntity)).thenReturn(mailingEntity);
        MailingEntity entity1 = mailingService.saveMailingEntity(mailingDTO);
        assertEquals(mailingDTO.getCountMails(), entity1.getCountMails());
        assertEquals(mailingDTO.getLastUsedTemplate(), entity1.getLastUsedTemplate());
        List<String> stringList1 = mailingDTO.getLastTemplates();
        for (String temp : stringList1) {
            assertTrue(entity1.getLastTemplates().contains(temp));
        }

    }
}