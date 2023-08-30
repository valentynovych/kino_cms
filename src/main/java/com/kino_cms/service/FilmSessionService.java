package com.kino_cms.service;

import com.kino_cms.entity.Film;
import com.kino_cms.entity.FilmSession;
import com.kino_cms.repository.FilmSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilmSessionService {
    @Autowired
    FilmSessionRepo filmSessionRepo;
    @Autowired
    FilmService filmService;

    public List<FilmSession> getAllSessionByFilmId(Long filmId) {
        Optional<Film> filmDtoById = filmService.getFilmById(filmId);
        Film film;
        List<FilmSession> filmSessionList;
        if (filmDtoById.isPresent()) {
            film = filmDtoById.get();
            filmSessionList = filmSessionRepo.getFilmSessionByFilm(film).stream().distinct().toList();
        } else {
            filmSessionList = new ArrayList<>();
        }
        return filmSessionList;
    }
}
