package com.kino_cms.service;

import com.kino_cms.entity.HomePage;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.HomePageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final HomePageRepo homePageRepo;

    public void save(HomePage homePage) {
        homePageRepo.save(homePage);
    }

    public Optional<HomePage> getHomePageByLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().equals("en")) {
            return homePageRepo.findByLanguage(Language.ENGLISH);
        }
        return homePageRepo.findByLanguage(Language.UKRAINIAN);
    }

    public Optional<HomePage> getHomePageByLanguageOrId(Language language, Long pageId) {
        if (language != null) {
            return homePageRepo.findByLanguage(language);
        }
        return homePageRepo.findById(pageId);

    }
}
