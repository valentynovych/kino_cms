package com.kino_cms.controller.users;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.service.FilmService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FilmPageViewController {
    @Autowired
    FilmService filmService;

    @GetMapping("/film/{id}")
    public String viewFilm(Model model, @PathVariable Long id) {
        Optional<FilmDTO> filmDtoById = filmService.getFilmDtoById(id);
        List<String> filmImages = new ArrayList<>();
        if (filmDtoById.isPresent()) {
            FilmDTO filmDTO = filmDtoById.get();
            model.addAttribute("filmDtoById", filmDTO);

            filmImages.addAll(List.of(filmDTO.getImage1(), filmDTO.getImage2(),
                    filmDTO.getImage3(), filmDTO.getImage4(), filmDTO.getImage5()));
            model.addAttribute("filmImages", filmImages
                    .stream()
                    .filter(Strings::isNotEmpty)
                    .toList());
        } else {
            model.addAttribute("filmDtoById", new FilmDTO());
            model.addAttribute("filmImages", filmImages);
        }
        return "user_views/filmViewPage";
    }
}
