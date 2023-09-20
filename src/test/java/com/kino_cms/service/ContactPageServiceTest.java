package com.kino_cms.service;

import com.kino_cms.entity.ContactCinema;
import com.kino_cms.entity.ContactPage;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import com.kino_cms.repository.ContactPageRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactPageServiceTest {

    @Mock
    ContactPageRepo contactPageRepo;
    ContactPageService contactPageService;
    private ContactPage contactPage;
    @BeforeEach
    void setUp() {
        contactPageService = new ContactPageService(contactPageRepo);
        contactPage = new ContactPage();
        contactPage.setPageType(PageType.CONTACT_PAGE);
        contactPage.setId(0L);
        contactPage.setLanguage(Language.UKRAINIAN);
        contactPage.setTitle("contactPageTitle");
        contactPage.setCreateTime(SimpleDateFormat.getDateInstance().format(new Date()));
        contactPage.setIsActive(Boolean.TRUE);
        SeoBlock seoBlock = new SeoBlock();
        seoBlock.setSeoTitle("seoTitle");
        contactPage.setSeoBlock(seoBlock);
        ContactCinema contactCinema = new ContactCinema();
        contactCinema.setContactPage(contactPage);
        contactCinema.setLogo_image("logoContact");
        contactCinema.setAddress("address");
        contactCinema.setIsActivate(Boolean.TRUE);
        contactCinema.setCoordinates("coordinates");
        contactPage.setContactCinemaList(List.of(contactCinema, new ContactCinema()));
    }

    @Test
    void getContactPage() {
        contactPage.setId(1L);
        when(contactPageRepo.findById(1L)).thenReturn(Optional.of(contactPage));

        Optional<ContactPage> contactPage1 = contactPageService.getContactPage();
        assertTrue(contactPage1.isPresent());
        ContactPage contactPage2 = contactPage1.get();
        assertEquals(contactPage.getPageType(), contactPage2.getPageType());
        assertEquals(contactPage.getContactCinemaList().size(), contactPage2.getContactCinemaList().size());
        assertEquals(contactPage.getLanguage(), contactPage2.getLanguage());
        assertEquals(contactPage.getTitle(), contactPage2.getTitle());

    }

    @Test
    void save() {
        when(contactPageRepo.save(contactPage)).thenReturn(contactPage);
        ContactPage save = contactPageService.save(contactPage);
        assertNotNull(save.getId());
        assertEquals(contactPage.getPageType(), save.getPageType());
        assertEquals(contactPage.getLanguage(), save.getLanguage());
    }
}