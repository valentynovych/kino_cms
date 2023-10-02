package com.kino_cms.service;

import com.kino_cms.entity.Film;
import com.kino_cms.entity.FilmDetails;
import com.kino_cms.repository.FilmDetailsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class FilmDetailsService {

    private final FilmDetailsRepo filmDetailsRepo;
    private final FilmService filmService;

    public FilmDetails getFilmDetailsByFilmId(Long filmId) {
        log.info("-> start execution method getFilmDetailsByFilmId() by id: " + filmId);
        Optional<Film> filmOptional = filmService.getFilmById(filmId);
        Film film;
        if (filmOptional.isPresent()) {
            log.info("-> getFilmDetailsByFilmId > film is present");
            film = filmOptional.get();
        } else {
            log.info("-> getFilmDetailsByFilmId > create new film with id: " + filmId);
            film = new Film();
            film.setId(filmId);
        }

        Optional<FilmDetails> filmDetailsOptional = filmDetailsRepo.getFilmDetailsByFilmId(film);
        FilmDetails filmDetails;
        if (filmDetailsOptional.isPresent()) {
            log.info("-> getFilmDetailsByFilmId > filmDetails is present");
            filmDetails = filmDetailsOptional.get();
        } else {
            log.info("-> getFilmDetailsByFilmId > create new filmDetails with filmId: " + filmId);
            filmDetails = new FilmDetails();
            filmDetails.setFilm(film);
        }
        log.info("-> exit from method getFilmDetailsByFilmId()");
        return filmDetails;
    }

    public void saveFilmDetails(FilmDetails filmDetails) {
        log.info("-> start execution method saveFilmDetails()");
        filmDetailsRepo.save(filmDetails);
        log.info("-> exit from method saveFilmDetails()");
    }
}
