package com.kino_cms.controller.users;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.HomePage;
import com.kino_cms.service.GeneralPageService;
import com.kino_cms.service.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@ControllerAdvice
@RequiredArgsConstructor
public class AttributeController {
    private final HomePageService homePageService;
    private final GeneralPageService generalPageService;

    @ModelAttribute("phone_main")
    public String getPhoneName(Locale locale) {
        Optional<HomePage> homePageById = homePageService.getHomePageByLocale(locale);
        if (homePageById.isPresent()) {
            return homePageById.get().getPhone_main();
        } else {
            return "";
        }
    }

    @ModelAttribute("phone_other")
    public String getPhoneOther(Locale locale) {
        Optional<HomePage> homePageById = homePageService.getHomePageByLocale(locale);
        if (homePageById.isPresent()) {
            return homePageById.get().getPhone_other();
        } else {
            return "";
        }
    }

    @ModelAttribute("other_pages")
    public List<GeneralPageDTO> getOtherPages(Locale locale) {
        List<GeneralPageDTO> pages = generalPageService.getAllOtherPages(locale);
        return pages;
    }

    @ModelAttribute("list_about_pages")
    public List<GeneralPage> getListAboutPages(Locale locale) {
        List<GeneralPage> allUkPageByPageTypeForMenu = generalPageService.getAllPageByPageTypeForMenu(locale);
        return allUkPageByPageTypeForMenu;
    }
}
