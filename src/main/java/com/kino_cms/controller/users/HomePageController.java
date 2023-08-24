package com.kino_cms.controller.users;

import com.kino_cms.dto.BannerDTO;
import com.kino_cms.dto.FilmDTO;
import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.dto.Page;
import com.kino_cms.entity.HomePage;
import com.kino_cms.service.BannerService;
import com.kino_cms.service.FilmService;
import com.kino_cms.service.GeneralPageService;
import com.kino_cms.service.HomePageService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

@Controller
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
    public String viewHomePage(Model model){

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
    @ModelAttribute("phone_main")
    public String getPhoneName(){
        Optional<HomePage> homePageById = homePageService.getHomePageById(2L);
        if (homePageById.isPresent()) {
            return homePageById.get().getPhone_main();
        } else {
            return "";
        }
    }
    @ModelAttribute("phone_other")
    public String getPhoneOther(){
        Optional<HomePage> homePageById = homePageService.getHomePageById(2L);
        if (homePageById.isPresent()) {
            return homePageById.get().getPhone_other();
        } else {
            return "";
        }
    }
    @ModelAttribute("other_pages")
    public List<GeneralPageDTO> getOtherPages(){
        List<GeneralPageDTO> pages = generalPageService.getAllOtherPages();
        return pages;
    }
}
