package com.kino_cms.service;

import com.kino_cms.dto.MailingDTO;
import com.kino_cms.entity.MailingEntity;
import com.kino_cms.repository.MailingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailingService {
    private final MailingRepository mailingRepository;

    public MailingDTO getMailingEntity() {
        log.info("-> start execution method getMailingEntity()");
        Optional<MailingEntity> mailingEntity = mailingRepository.findById(1L);
        MailingDTO dto = new MailingDTO();
        if (mailingEntity.isPresent()) {
            MailingEntity entity = mailingEntity.get();
            dto.setCountMails(entity.getCountMails());
            dto.setLastTemplates(Arrays
                    .stream(entity
                            .getLastTemplates()
                            .split(","))
                    .toList());
            dto.setLastUsedTemplate(entity.getLastUsedTemplate());
        } else {
            dto.setCountMails(0);
            dto.setLastUsedTemplate("");
        }
        log.info("-> exit from method getMailingEntity()");
        return dto;
    }

    public MailingEntity saveMailingEntity(MailingDTO dto) {
        log.info("-> start execution method saveMailingEntity()");
        Optional<MailingEntity> entity = mailingRepository.findById(1L);
        MailingEntity mailingEntity;
        if (entity.isPresent()) {
            mailingEntity = entity.get();
        } else {
            mailingEntity = new MailingEntity();
        }
        mailingEntity.setCountMails(dto.getCountMails());
        mailingEntity.setLastTemplates(String.join(",", dto.getLastTemplates()));
        mailingEntity.setLastUsedTemplate(dto.getLastUsedTemplate());
        log.info("-> exit from method saveMailingEntity()");
        return mailingRepository.save(mailingEntity);
    }


}
