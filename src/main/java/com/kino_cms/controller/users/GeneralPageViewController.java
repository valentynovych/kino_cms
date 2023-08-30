package com.kino_cms.controller.users;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.enums.PageType;
import com.kino_cms.service.GeneralPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GeneralPageViewController {

    @Autowired
    GeneralPageService generalPageService;

    @GetMapping("/page/{pageType}/{pageId}")
    public String generalPageDispatcher(@PathVariable("pageType") PageType pageType,
                                        @PathVariable("pageId") Long pageId) {
        switch (pageType) {
            case CONTACT_PAGE -> {
                return "redirect:/contacts/" + pageId;
            }
            case ADVERTISING, VIP_HALL, CAFE_BAR, CHILD_ROOM, OTHER_PAGE -> {
                return "redirect:/general/" + pageId;
            }
            default -> {
                return "redirect:/";
            }
        }
    }

    @GetMapping("/general/{pageId}")
    public ModelAndView generalPageView(@PathVariable("pageId") Long pageId) {
        ModelAndView modelAndView = new ModelAndView("user_views/generalPageView");
        modelAndView.addObject("generalPage", generalPageService.getGeneralPageDTOById(pageId).get());
        return modelAndView;
    }

    @GetMapping("/about-cinema")
    public ModelAndView aboutCinemaPageView() {
        ModelAndView modelAndView = new ModelAndView("user_views/generalPageView");
        GeneralPageDTO generalPage = generalPageService.getGeneralPageDTOAboutCinema();
        modelAndView.addObject("generalPage", generalPage);
        return modelAndView;
    }
}
