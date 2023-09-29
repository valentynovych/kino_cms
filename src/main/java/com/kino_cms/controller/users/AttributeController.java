package com.kino_cms.controller.users;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.HomePage;
import com.kino_cms.service.GeneralPageService;
import com.kino_cms.service.HomePageService;
import com.kino_cms.service.UserSessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@ControllerAdvice(basePackageClasses = {CinemaPageViewController.class,
        ContactCinemaPageViewController.class,
        FeedPageViewController.class,
        FilmPageViewController.class,
        GeneralPageViewController.class,
        HomePageController.class,
        PosterPageController.class,
        TimeTableViewPageController.class,
        UserPageController.class})
@RequiredArgsConstructor
@Log4j2
public class AttributeController {
    private final HomePageService homePageService;
    private final GeneralPageService generalPageService;
    private final UserSessionService userSessionService;

    @ModelAttribute("phone_main")
    public String getPhoneName() {
        Optional<HomePage> homePageById = homePageService.getHomePageByLocale();
        if (homePageById.isPresent()) {
            return homePageById.get().getPhone_main();
        } else {
            return "";
        }
    }

    @ModelAttribute("phone_other")
    public String getPhoneOther() {
        Optional<HomePage> homePageById = homePageService.getHomePageByLocale();
        if (homePageById.isPresent()) {
            return homePageById.get().getPhone_other();
        } else {
            return "";
        }
    }

    @ModelAttribute("other_pages")
    public List<GeneralPageDTO> getOtherPages() {
        List<GeneralPageDTO> pages = generalPageService.getAllOtherPages()
                .stream().filter(GeneralPageDTO::getIsActive).toList();
        return pages;
    }

    @ModelAttribute("list_about_pages")
    public List<GeneralPage> getListAboutPages(Locale locale) {
        List<GeneralPage> allUkPageByPageTypeForMenu = generalPageService.getAllPageByPageTypeForMenu(locale)
                .stream().filter(GeneralPage::getIsActive).toList();
        return allUkPageByPageTypeForMenu;
    }
    @ModelAttribute("link_to_about_page")
    public Boolean getLinkToAboutPage() {
        GeneralPageDTO generalPage = generalPageService.getGeneralPageDTOAboutCinema();
        if (generalPage != null && generalPage.getIsActive()) {
            return true;
        }
        return false;
    }

    @ModelAttribute
    public void logSession(HttpSession httpSessionListener) {
        userSessionService.createSession(httpSessionListener);
    }
}
