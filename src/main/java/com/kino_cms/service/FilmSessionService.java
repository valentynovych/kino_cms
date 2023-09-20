package com.kino_cms.service;

import com.kino_cms.entity.Film;
import com.kino_cms.entity.FilmSession;
import com.kino_cms.repository.FilmSessionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class FilmSessionService {
    private final FilmSessionRepo filmSessionRepo;
    private final FilmService filmService;

    public List<FilmSession> getAllSessionByFilmId(Long filmId) {
        log.info("-> start execution method getAllSessionByFilmId() by id: " + filmId);
        Optional<Film> filmDtoById = filmService.getFilmById(filmId);
        Film film;
        List<FilmSession> filmSessionList;
        if (filmDtoById.isPresent()) {
            log.info("-> getAllSessionByFilmId(), film by id isPreset, get film session list by film");
            film = filmDtoById.get();
            filmSessionList = filmSessionRepo.getFilmSessionByFilm(film);
        } else {
            log.info("-> getAllSessionByFilmId(), film by id isEmpty, create empty list");
            filmSessionList = new ArrayList<>();
        }
        log.info("-> exit from method getAllSessionByFilmId(), return list size: " + filmSessionList.size());
        return filmSessionList;
    }

    public List<FilmSession> getAllSessionByCinemaId(Long cinemaId) {
        log.info("-> start execution method getAllSessionByCinemaId() by id: " + cinemaId);
        List<FilmSession> filmSessions = filmSessionRepo.getFilmSessionByCinema(cinemaId);
        log.info("-> exit from method getAllSessionByCinemaId(), return list size: " + filmSessions.size());
        return filmSessions;
    }

    public List<FilmSession> getAllSession() {
        log.info("-> start execution method getAllSession()");
        List<FilmSession> filmSessionList = filmSessionRepo.findAll();
        log.info("-> exit from method getAllSessionByCinemaId(), return list size: " + filmSessionList.size());
        return filmSessionList;
    }

    public FilmSession getSessionById(Long sessionId) {
        log.info("-> start execution method getSessionById() by id: " + sessionId);
        Optional<FilmSession> sessionById = filmSessionRepo.findById(sessionId);
        if (sessionById.isPresent()) {
            log.info("-> getAllSessionByFilmId(), FilmSession by id isPreset, return FilmSession");
            return sessionById.get();
        } else {
            log.info("-> getAllSessionByFilmId(), FilmSession by id isEmpty, return new FilmSession()");
            return new FilmSession();
        }

    }
}
