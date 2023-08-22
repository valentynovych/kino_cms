package com.kino_cms.service;

import com.kino_cms.dto.MailingDTO;
import com.kino_cms.entity.MailingEntity;
import com.kino_cms.repository.MailingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MailingService {
    @Autowired
    MailingRepository mailingRepository;

    public MailingDTO getMailingEntity() {
        Optional<MailingEntity> mailingEntity = mailingRepository.findById(1L);
        MailingDTO dto = new MailingDTO();
        if (mailingEntity.isPresent()) {

            MailingEntity entity = mailingEntity.get();
            dto.setCountMails(entity.getCountMails());
            dto.setLastTemplates(Arrays
                    .stream(entity
                            .getLastTemplates()
                            .split(" "))
                    .toList());
            dto.setLastUsedTemplate(entity.getLastUsedTemplate());
        }
        return dto;
    }

    public void saveMailingEntity(MailingDTO dto) {
        Optional<MailingEntity> entity = mailingRepository.findById(1L);
        MailingEntity mailingEntity;
        if (entity.isPresent()) {
            mailingEntity = entity.get();
        } else {
            mailingEntity = new MailingEntity();
        }
        mailingEntity.setCountMails(dto.getCountMails());
        mailingEntity.setLastTemplates(String.join(" ", dto.getLastTemplates()));
        mailingEntity.setLastUsedTemplate(dto.getLastUsedTemplate());
        mailingRepository.save(mailingEntity);
    }


}
