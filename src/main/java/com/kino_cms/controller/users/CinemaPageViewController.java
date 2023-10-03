package com.kino_cms.controller.users;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.dto.HallDTO;
import com.kino_cms.entity.FilmSession;
import com.kino_cms.service.CinemaService;
import com.kino_cms.service.FilmSessionService;
import com.kino_cms.service.HallService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("cinemas")
public class CinemaPageViewController {

    private final CinemaService cinemaService;
    private final HallService hallService;
    private final FilmSessionService filmSessionService;

    @GetMapping("")
    public ModelAndView viewAllCinemas() {
        ModelAndView modelAndView = new ModelAndView("user_views/cinemas/allCinemaViewPage");
        List<CinemaDTO> allCinemaDto = cinemaService.getAllCinemaDto();
        modelAndView.addObject("allCinemaList", allCinemaDto);
        return modelAndView;
    }

    @GetMapping("{cinemaId}")
    public ModelAndView viewCinema(@PathVariable Long cinemaId) {
        ModelAndView modelAndView = new ModelAndView("user_views/cinemas/cinemaViewPage");
        CinemaDTO cinemaDto = cinemaService.getPresentCinemaDtoById(cinemaId);
        modelAndView.addObject("cinemaDto", cinemaDto);

        List<HallDTO> allHallByCinema = hallService.getAllHallByCinema(cinemaDto);
        modelAndView.addObject("allHallByCinema", allHallByCinema);

        List<FilmSession> allSessionByCinemaId = filmSessionService.getAllSessionByCinemaId(cinemaId);
        modelAndView.addObject("allSessionByCinema", allSessionByCinemaId);

        List<String> listImagesFileNameById = cinemaService.getListImagesFileNameById(cinemaId);
        modelAndView.addObject("listImages", listImagesFileNameById
                .stream()
                .filter(Objects::nonNull)
                .filter(Strings::isNotEmpty)
                .toList());
        return modelAndView;
    }

    @GetMapping("hall/{hallId}")
    public ModelAndView viewHall(@PathVariable Long hallId) {
        ModelAndView modelAndView = new ModelAndView("user_views/cinemas/hallViewPage");
        Optional<HallDTO> hallDtoById = hallService.getHallDtoById(hallId);

        HallDTO hallDTO = hallDtoById.orElseGet(HallDTO::new);
        modelAndView.addObject("hallDTO", hallDTO);

        List<FilmSession> allSessionByCinemaId = filmSessionService.getAllSessionByCinemaId(hallId);
        modelAndView.addObject("allSessionByCinema", allSessionByCinemaId);

        List<String> listImagesFileNameById = hallService.getListImagesFileNameById(hallId);
        modelAndView.addObject("listImages", listImagesFileNameById);
        return modelAndView;
    }
}
