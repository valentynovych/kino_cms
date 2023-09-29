package com.kino_cms.service;

import com.kino_cms.entity.HomePage;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import com.kino_cms.repository.HomePageRepo;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class HomePageServiceTest {

    @Mock
    private HomePageRepo homePageRepo;
    private HomePageService homePageService;
    private static HomePage homePage;

    @BeforeEach
    void init() {
        homePageService = new HomePageService(homePageRepo);
        homePage = new HomePage();
        homePage.setPageType(PageType.HOME_PAGE);
        homePage.setLanguage(Language.UKRAINIAN);
        homePage.setTitle("HomePageTest");
        homePage.setSeoText(RandomString.make(5_000));
        homePage.setPhone_main("+38 (067) 00-00 000");
    }

    @Test
    void save() {
        Mockito.when(homePageRepo.findById(1L)).thenReturn(Optional.of(homePage));
        Mockito.when(homePageRepo.save(homePage)).thenReturn(homePage);
        HomePage save = homePageService.save(homePage);

        Optional<HomePage> homePageOptional = homePageService.getHomePageByLanguageOrId(null, 1L);
        Assertions.assertTrue(homePageOptional.isPresent());
        Assertions.assertEquals(save.getLanguage(), homePage.getLanguage());
        Assertions.assertEquals(save.getPageType(), homePage.getPageType());
        Assertions.assertEquals(save.getSeoText(), homePage.getSeoText());

    }

    @Test
    void getHomePageByLocale() {
        Mockito.when(homePageRepo.findByLanguage(Language.UKRAINIAN)).thenReturn(Optional.of(homePage));
        Mockito.when(homePageRepo.findByLanguage(Language.ENGLISH)).thenReturn(Optional.empty());

        Optional<HomePage> homePageOptional = homePageService.getHomePageByLanguageOrId(Language.UKRAINIAN, null);
        Assertions.assertTrue(homePageOptional.isPresent());
        HomePage homePage1 = homePageOptional.get();
        Assertions.assertEquals(homePage.getLanguage(), homePage1.getLanguage());
        Assertions.assertEquals(homePage.getPageType(), homePage1.getPageType());
        Assertions.assertEquals(homePage.getSeoText(), homePage1.getSeoText());

        Optional<HomePage> homePageOptional1 = homePageService.getHomePageByLanguageOrId(Language.ENGLISH, null);
        Assertions.assertTrue(homePageOptional1.isEmpty());
    }

    @Test
    void getHomePageByLanguageOrId() {
        Mockito.when(homePageRepo.findByLanguage(Language.UKRAINIAN)).thenReturn(Optional.of(homePage));
        Mockito.when(homePageRepo.findByLanguage(Language.ENGLISH)).thenReturn(Optional.empty());

        LocaleContextHolder.setLocale(Locale.forLanguageTag("uk"));
        Optional<HomePage> homePageOptional = homePageService.getHomePageByLocale();
        Assertions.assertTrue(homePageOptional.isPresent());
        HomePage homePage1 = homePageOptional.get();
        Assertions.assertEquals(homePage.getLanguage(), homePage1.getLanguage());
        Assertions.assertEquals(homePage.getPageType(), homePage1.getPageType());
        Assertions.assertEquals(homePage.getSeoText(), homePage1.getSeoText());

        LocaleContextHolder.setLocale(Locale.forLanguageTag("en"));
        Optional<HomePage> homePageOptional1 = homePageService.getHomePageByLocale();
        Assertions.assertFalse(homePageOptional1.isEmpty());
        HomePage homePage2 = homePageOptional1.get();
        Assertions.assertEquals(Language.UKRAINIAN, homePage2.getLanguage());
    }
}