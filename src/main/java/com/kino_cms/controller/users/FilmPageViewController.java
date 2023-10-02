package com.kino_cms.controller.users;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.entity.FilmDetails;
import com.kino_cms.entity.FilmSession;
import com.kino_cms.service.CinemaService;
import com.kino_cms.service.FilmDetailsService;
import com.kino_cms.service.FilmService;
import com.kino_cms.service.FilmSessionService;
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
    @Autowired
    FilmDetailsService filmDetailsService;
    @Autowired
    CinemaService cinemaService;

    @Autowired
    FilmSessionService filmSessionService;

    @GetMapping("/film/{id}")
    public String viewFilm(Model model, @PathVariable Long id) {
        Optional<FilmDTO> filmDtoById = filmService.getFilmDtoById(id);
        List<String> filmImages = new ArrayList<>();
        FilmDetails filmDetails;
        List<FilmSession> filmSessionList;

        if (filmDtoById.isPresent()) {
            FilmDTO filmDTO = filmDtoById.get();

            String videoUrl = filmDTO.getUrlVideo();
            if (Strings.isNotEmpty(videoUrl)) {
                int length = videoUrl.length();
                String videoId = videoUrl.substring(videoUrl.indexOf("=") + 1, length);
                filmDTO.setUrlVideo("https://www.youtube.com/embed/" + videoId);
            }
            model.addAttribute("filmDto", filmDTO);

            filmDetails = filmDetailsService.getFilmDetailsByFilmId(filmDTO.getId());
            model.addAttribute("filmDetails", filmDetails);
            filmImages.addAll(List.of(filmDTO.getImage1(), filmDTO.getImage2(),
                    filmDTO.getImage3(), filmDTO.getImage4(), filmDTO.getImage5()));
            model.addAttribute("filmImages", filmImages
                    .stream()
                    .filter(Strings::isNotEmpty)
                    .toList());

            filmSessionList = filmSessionService.getAllSessionByFilmId(filmDTO.getId());
            model.addAttribute("filmSessionList", filmSessionList);

        } else {
            model.addAttribute("filmDto", new FilmDTO());
            model.addAttribute("filmImages", filmImages);
            model.addAttribute("filmDetails", new FilmDetails());
            model.addAttribute("filmSessionList", new ArrayList<FilmSession>());
        }
        return "user_views/filmViewPage";
    }
}
