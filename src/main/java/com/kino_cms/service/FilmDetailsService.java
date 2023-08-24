package com.kino_cms.service;

import com.kino_cms.entity.Film;
import com.kino_cms.entity.FilmDetails;
import com.kino_cms.repository.FilmDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FilmDetailsService {

    @Autowired
    FilmDetailsRepo filmDetailsRepo;
    @Autowired
    private FilmService filmService;

    public FilmDetails getFilmDetailsByFilmId(Long filmId) {
        Optional<Film> filmOptional = filmService.getFilmById(filmId);
        Film film;
        if (filmOptional.isPresent()) {
            film = filmOptional.get();
        } else {
            film = new Film();
            film.setId(filmId);
        }

        Optional<FilmDetails> filmDetailsOptional = filmDetailsRepo.getFilmDetailsByFilmId(film);
        FilmDetails filmDetails;
        if (filmDetailsOptional.isPresent()) {
            filmDetails = filmDetailsOptional.get();
        } else {
            filmDetails = new FilmDetails();
            filmDetails.setFilm(film);
        }
        return filmDetails;
    }

    public void saveFilmDetails(FilmDetails filmDetails) {
        filmDetailsRepo.save(filmDetails);
    }
}
