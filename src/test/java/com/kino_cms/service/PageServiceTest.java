package com.kino_cms.service;

import com.kino_cms.dto.Page;
import com.kino_cms.entity.ContactPage;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.HomePage;
import com.kino_cms.enums.PageType;
import com.kino_cms.repository.ContactPageRepo;
import com.kino_cms.repository.GeneralPageRepo;
import com.kino_cms.repository.HomePageRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PageServiceTest {

    @Mock
    private HomePageRepo homePageRepo;
    @Mock
    private GeneralPageRepo generalPageRepo;
    @Mock
    private ContactPageRepo contactPageRepo;
    private PageService pageService;

    @BeforeEach
    void init(){
        pageService = new PageService(generalPageRepo, homePageRepo, contactPageRepo);
        HomePage homePage1 = new HomePage();
        homePage1.setPageType(PageType.HOME_PAGE);
        GeneralPage generalPage1 = new GeneralPage();
        generalPage1.setPageType(PageType.OTHER_PAGE);
        ContactPage contactPage1 =new ContactPage();
        contactPage1.setPageType(PageType.CONTACT_PAGE);

        Mockito.when(homePageRepo.findAll()).thenReturn(List.of(homePage1));
        Mockito.when(generalPageRepo.findAll()).thenReturn(List.of(generalPage1));
        Mockito.when(contactPageRepo.findAll()).thenReturn(List.of(contactPage1));

    }
    @Test
    void getAllPages() {
        List<Page> pages = pageService.getAllPages();
        Assertions.assertFalse(pages.isEmpty());
        Assertions.assertTrue(pages.size() >= 3);

        List<PageType> pageTypes = pages.stream().map(Page::getPageType).toList();
        Assertions.assertTrue(pageTypes.contains(PageType.HOME_PAGE));
        Assertions.assertTrue(pageTypes.contains(PageType.CONTACT_PAGE));
        Assertions.assertTrue(pageTypes.contains(PageType.OTHER_PAGE));

    }
}