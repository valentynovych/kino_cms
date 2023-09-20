package com.kino_cms.service;

import com.kino_cms.entity.ContactPage;
import com.kino_cms.repository.ContactPageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ContactPageService {

    private final ContactPageRepo contactPageRepo;

    public Optional<ContactPage> getContactPage() {
        log.info("-> start execution method getContactPage()");
        Optional<ContactPage> byId = contactPageRepo.findById(1L);
        log.info("-> exit from method getContactPage(), return optional isPresent: " + byId.isPresent());
        return byId;
    }

    public ContactPage save(ContactPage contactPage) {
        log.info("-> start execution method save(ContactPage)");
        ContactPage save = contactPageRepo.save(contactPage);
        log.info("-> success saving ContactPage");
        return save;
    }

}
