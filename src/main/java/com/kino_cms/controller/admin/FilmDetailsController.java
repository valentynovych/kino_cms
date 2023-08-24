package com.kino_cms.controller.admin;

import com.kino_cms.entity.FilmDetails;
import com.kino_cms.service.FilmDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FilmDetailsController {

    @Autowired
    FilmDetailsService filmDetailsService;

    @GetMapping("/admin/edit-details/{filmId}")
    public String editFilmDetails(Model model, @PathVariable Long filmId) {
        FilmDetails filmDetailsByFilmId = filmDetailsService.getFilmDetailsByFilmId(filmId);
        model.addAttribute("filmDetails", filmDetailsByFilmId);
        return "admin/films/editFilmDetails";
    }

    @PostMapping("/admin/edit-details/{filmId}")
    public String saveFilmDetails(@PathVariable Long filmId,
                                  @ModelAttribute("filmDetails") FilmDetails filmDetails) {
        filmDetailsService.saveFilmDetails(filmDetails);

        return "redirect:/admin/edit-film/" + filmId;
    }
}
