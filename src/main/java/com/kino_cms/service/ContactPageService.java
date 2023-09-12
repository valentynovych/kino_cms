package com.kino_cms.service;

import com.kino_cms.entity.ContactPage;
import com.kino_cms.repository.ContactPageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactPageService {
    @Autowired
    ContactPageRepo contactPageRepo;

    public Optional<ContactPage> getContactPage() {
        return contactPageRepo.findById(1L);
    }

    public void save(ContactPage contactPage) {
        contactPageRepo.save(contactPage);
    }

}
