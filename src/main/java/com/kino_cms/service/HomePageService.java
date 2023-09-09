package com.kino_cms.service;

import com.kino_cms.entity.HomePage;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.HomePageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class HomePageService {
    @Autowired
    HomePageRepo homePageRepo;

    public Optional<HomePage> getHomePage(Long id) {

        return homePageRepo.findByLanguage(Language.ENGLISH);
    }

    public void save(HomePage homePage) {
        homePageRepo.save(homePage);
    }

    public Optional<HomePage> getHomePageByLocale(Locale locale) {
        if (locale.getLanguage().equals("en")) {
            return homePageRepo.findByLanguage(Language.ENGLISH);
        }
        return homePageRepo.findByLanguage(Language.UKRAINIAN);
    }
}
