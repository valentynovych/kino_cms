package com.kino_cms.controller.users;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PosterPageController {
    @Autowired
    FilmService filmService;
    @GetMapping("/poster")
    public String viewPoster(){

        return "redirect:/poster/now";
    }
    @GetMapping("/poster/now")
    public String viewPosterNow(Model model){
        List<FilmDTO> filmIsReleasedNow = filmService.getAllFilmIsReleasedNow();
        model.addAttribute("filmList", filmIsReleasedNow);

        return "user_views/posterViewPage";
    }

    @GetMapping("/poster/soon")
    public String viewPosterSoon(Model model){
        List<FilmDTO> filmReleasedSoon = filmService.getAllFilmReleasedSoon();
        model.addAttribute("filmList", filmReleasedSoon);

        return "user_views/posterViewPage";
    }
}
