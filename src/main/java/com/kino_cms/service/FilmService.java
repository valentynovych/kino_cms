package com.kino_cms.service;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.entity.Film;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.FilmType;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.FilmRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class FilmService {
    private final FilmRepo filmRepo;

    public void saveFilm(Film film) {
        log.info("-> start execution method saveFilm()");
        if (film.getTranslatePageId() != null) {
            Film save = filmRepo.save(film);
            log.info("-> saveFilm() success saving Film with id:" + save.getId());
            Film toSetTranslate = filmRepo.findById(save.getTranslatePageId()).get();
            toSetTranslate.setTranslatePageId(save.getId());
            filmRepo.save(toSetTranslate);
            log.info("-> saveFilm() success update relation translate page Film with translateId: " + toSetTranslate.getId());
        } else {
            filmRepo.save(film);
            log.info("-> saveFilm() success saving Film");
        }
    }

    public Optional<Film> getFilmById(Long id) {
        log.info("-> start execution method getFilmById() by id: " + id);
        Optional<Film> byId = filmRepo.findById(id);
        log.info("-> exit from method getFilmById(), filmOptional isPresent: " + byId.isPresent());
        return byId;
    }

    public Optional<FilmDTO> getFilmDtoById(Long id) {
        log.info("-> start execution method getFilmDtoById() by id: " + id);
        Optional<FilmDTO> filmDtoById = filmRepo.getFilmDtoById(id);
        log.info("-> exit from method getFilmById(), filmOptional isPresent: " + filmDtoById.isPresent());
        return filmDtoById;
    }

    public List<FilmDTO> getAllFilmIsReleasedNow() {
        log.info("-> start execution method getAllFilmIsReleasedNow()");
        Locale locale = LocaleContextHolder.getLocale();
        List<FilmDTO> filmIsReleasedNow;
        log.info("-> getAllFilmIsReleasedNow() by locale: " + locale);
        if (locale.getLanguage().equals("en")) {
            filmIsReleasedNow = filmRepo.getAllFilmIsReleasedNow(Language.ENGLISH);
        } else {
            filmIsReleasedNow = filmRepo.getAllFilmIsReleasedNow(Language.UKRAINIAN);
        }
        log.info("-> exit method getAllFilmIsReleasedNow(), return listSize: " + filmIsReleasedNow.size());
        return filmIsReleasedNow;
    }

    public List<FilmDTO> getAllFilmReleasedSoon() {
        log.info("-> start execution method getAllFilmReleasedSoon()");
        Locale locale = LocaleContextHolder.getLocale();
        List<FilmDTO> filmReleasedSoon;
        log.info("-> getAllFilmReleasedSoon() by locale: " + locale);
        if (locale.getLanguage().equals("en")) {
            filmReleasedSoon = filmRepo.getAllFilmReleasedSoon(Language.ENGLISH);
        } else {
            filmReleasedSoon = filmRepo.getAllFilmReleasedSoon(Language.UKRAINIAN);
        }
        log.info("-> exit method getAllFilmReleasedSoon(), return listSize: " + filmReleasedSoon.size());
        return filmReleasedSoon;
    }

    public List<FilmType> getFilmTypeListById(Long id) {
        log.info("-> start execution method getFilmTypeListById()");
        Optional<Film> optionalFilm = getFilmById(id);
        List<FilmType> filmTypeList;

        if (optionalFilm.isPresent()) {
            log.info("-> getFilmTypeListById() > film isPresent, get FilmTypeList");
            Film film = optionalFilm.get();
            filmTypeList = film.getFilmTypeList();
        } else {
            log.info("-> getFilmTypeListById() > film isEmpty, create empty FilmTypeList");
            filmTypeList = new ArrayList<>();
        }
        log.info("-> exit from getFilmTypeListById()");
        return filmTypeList;
    }

    public List<String> getListImagesFileNameById(Long id) {
        log.info("-> start execution method getListImagesFileNameById()");
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", ""));
        Film film;
        Optional<Film> filmOptional = getFilmById(id);
        if (filmOptional.isPresent()) {
            film = filmOptional.get();
            log.info("-> getListImagesFileNameById > feedPage isPresent, get image from Film");
            fileNamesFromDB.set(0, film.getMainImage());
            fileNamesFromDB.set(1, film.getImage1());
            fileNamesFromDB.set(2, film.getImage2());
            fileNamesFromDB.set(3, film.getImage3());
            fileNamesFromDB.set(4, film.getImage4());
            fileNamesFromDB.set(5, film.getImage5());
        }
        log.info("-> exit from method getListImagesFileNameById()");
        return fileNamesFromDB;
    }

    public FilmDTO updateImagesOnModel(FilmDTO filmDTO, List<String> fileNamesFromDB) {
        log.info(String.format("-> start execution private method updateImagesOnModel(FilmDTO %s)", filmDTO.toString()));
        filmDTO.setMainImage(fileNamesFromDB.get(0));
        filmDTO.setImage1(fileNamesFromDB.get(1));
        filmDTO.setImage2(fileNamesFromDB.get(2));
        filmDTO.setImage3(fileNamesFromDB.get(3));
        filmDTO.setImage4(fileNamesFromDB.get(4));
        filmDTO.setImage5(fileNamesFromDB.get(5));
        log.info("-> success update image filename \n\t\t exit from method updateImagesOnModel()");
        return filmDTO;
    }

    public void saveFilmDTO(FilmDTO filmDTOModel) {
        log.info("-> start execution method saveFilmDTO(FilmDTO): " + filmDTOModel.toString());
        Optional<Film> filmOptional = getFilmById(filmDTOModel.getId());
        Film filmToSave;
        SeoBlock seoBlock;

        if (filmOptional.isPresent()) {
            log.info("-> saveFilmDTO > filmOptional isPresent");
            filmToSave = filmOptional.get();
            seoBlock = filmToSave.getSeoBlock();
        } else {
            log.info("-> saveFilmDTO > filmOptional isEmpty, create new Film/SeoBlock");
            filmToSave = new Film();
            seoBlock = new SeoBlock();
        }
        String dateFromTo = filmDTOModel.getDatePremiereFromTo();
        List<String> twoDate = Arrays.stream(dateFromTo.split(" ")).toList();
        if (twoDate.size() > 2) {
            filmToSave.setDateOfPremiere(Date.valueOf(twoDate.get(0)));
            filmToSave.setDateEndPremiere(Date.valueOf(twoDate.get(2)));
        }
        filmToSave.setName(filmDTOModel.getName());
        filmToSave.setDescription(filmDTOModel.getDescription());
        filmToSave.setMainImage(filmDTOModel.getMainImage());
        filmToSave.setImage1(filmDTOModel.getImage1());
        filmToSave.setImage2(filmDTOModel.getImage2());
        filmToSave.setImage3(filmDTOModel.getImage3());
        filmToSave.setImage4(filmDTOModel.getImage4());
        filmToSave.setImage5(filmDTOModel.getImage5());
        filmToSave.setUrlVideo(filmDTOModel.getUrlVideo());
        filmToSave.setFilmTypeList(filmDTOModel.getFilmTypeList());
        filmToSave.setLanguage(filmDTOModel.getLanguage());

        seoBlock.setSeoUrl(filmDTOModel.getSeoUrl());
        seoBlock.setSeoTitle(filmDTOModel.getSeoTitle());
        seoBlock.setSeoKeywords(filmDTOModel.getSeoKeywords());
        seoBlock.setSeoDescription(filmDTOModel.getSeoDescription());
        filmToSave.setSeoBlock(seoBlock);
        filmToSave.setDatePremiereFromTo(filmDTOModel.getDatePremiereFromTo());
        filmToSave.setTranslatePageId(filmDTOModel.getTranslatePageId());

        saveFilm(filmToSave);
        log.info("-> exit from method saveFilmDTO()");
    }

    public Optional<FilmDTO> getFilmDtoByTranslatePageId(Long id) {
        log.info("-> start execution method getFilmDtoByTranslatePageId()");
        Optional<FilmDTO> filmDtoByTranslatePageId = filmRepo.getFilmDtoByTranslatePageId(id);
        log.info("-> start execution method getFilmDtoByTranslatePageId(), translatePage isPresent: " + filmDtoByTranslatePageId.isPresent());
        return filmDtoByTranslatePageId;
    }
}
