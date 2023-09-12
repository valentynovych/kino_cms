package com.kino_cms.controller.users;

import com.kino_cms.dto.BannerDTO;
import com.kino_cms.dto.FilmDTO;
import com.kino_cms.entity.HomePage;
import com.kino_cms.service.BannerService;
import com.kino_cms.service.FilmService;
import com.kino_cms.service.GeneralPageService;
import com.kino_cms.service.HomePageService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@ControllerAdvice
public class HomePageController {
    @Autowired
    HomePageService homePageService;
    @Autowired
    GeneralPageService generalPageService;
    @Autowired
    BannerService bannerService;
    @Autowired
    FilmService filmService;

    @GetMapping("/")
    public String viewHomePage(Model model) {

        Optional<HomePage> homePageById = homePageService.getHomePageByLocale();
        if (homePageById.isPresent()) {
            model.addAttribute("homePage", homePageById.get());
        }

        BannerDTO headerBanner = bannerService.getHeaderBanner();
        model.addAttribute("headerBanner", headerBanner);

        List<FilmDTO> filmIsReleasedNow = filmService.getAllFilmIsReleasedNow();
        List<FilmDTO> filmReleasedSoon = filmService.getAllFilmReleasedSoon();

        model.addAttribute("filmIsReleasedNow", filmIsReleasedNow);
        model.addAttribute("filmReleasedSoon", filmReleasedSoon);

        BannerDTO perforatingBanner = bannerService.getPerforatingBanner();
        model.addAttribute("perforatingBanner", perforatingBanner);

        BannerDTO promotionBanner = bannerService.getPromotionBanner();
        promotionBanner.setBannerImages(promotionBanner.getBannerImages()
                .stream()
                .filter(bannerImage -> Strings.isNotEmpty(bannerImage.getImage()))
                .toList());
        model.addAttribute("promotionBanner", promotionBanner);

        return "user_views/homePageView";
    }


}
