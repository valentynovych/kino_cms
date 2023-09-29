package com.kino_cms.service;

import com.kino_cms.entity.HomePage;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.HomePageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class HomePageService {

    private final HomePageRepo homePageRepo;

    public HomePage save(HomePage homePage) {
        log.info("-> start execution method save(HomePage homepage)");
        HomePage save = homePageRepo.save(homePage);
        log.info("-> success saved HomePage with new id: " + save.getId());
        return save;
    }

    public Optional<HomePage> getHomePageByLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        log.info("-> start execution method getHomePageByLocale() by locale: " + locale);
        if (locale.getLanguage().equals("en")) {
            log.info("-> returned HomePage by language: " + Language.ENGLISH.name());
            Optional<HomePage> byLanguage = homePageRepo.findByLanguage(Language.ENGLISH);
            if (byLanguage.isPresent()) {
                return byLanguage;
            }
        }
        log.info("-> returned HomePage by language: " + Language.UKRAINIAN.name());
        return homePageRepo.findByLanguage(Language.UKRAINIAN);
    }

    public Optional<HomePage> getHomePageByLanguageOrId(Language language, Long pageId) {
        log.info(String.format("-> start execution method " +
                "getHomePageByLanguageOrId(Language %s, Long %s)", language, pageId));
        if (language != null) {
            log.info("-> returned HomePage by language: " + language);
            return homePageRepo.findByLanguage(language);
        }
        log.info("-> returned HomePage by id: " + pageId);
        return homePageRepo.findById(pageId);

    }
}
