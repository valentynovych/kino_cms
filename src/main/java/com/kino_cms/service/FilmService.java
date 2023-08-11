package com.kino_cms.service;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.entity.Film;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.FilmType;
import com.kino_cms.repository.FilmRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    @Autowired
    FilmRepo filmRepo;
    @Autowired
    SaveUploadService uploadService;
    private static final Date today = new Date(System.currentTimeMillis());
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void saveFilm(Film film) {
        filmRepo.save(film);
    }

    public Optional<Film> getFilmById(Long id) {
        return filmRepo.findById(id);
    }

    public Optional<FilmDTO> getFilmDtoById(Long id) {
        return filmRepo.getFilmDtoById(id);
    }

    public List<FilmDTO> getAllFilmDto() {
        return filmRepo.getAllFilmDto();
    }

    public List<FilmDTO> getAllFilmIsReleasedNow() {
        List<FilmDTO> allFilms = getAllFilmDto();
        List<FilmDTO> filmIsReleasedNow = allFilms
                .stream()
                .filter(filmDTO -> filmDTO.getDateOfPremiere().before(Date.valueOf(today.toLocalDate().plusDays(1))))
                .toList();
        return filmIsReleasedNow;
    }

    public List<FilmDTO> getAllFilmReleasedSoon() {
        List<FilmDTO> allFilms = getAllFilmDto();
        List<FilmDTO> filmReleasedSoon = allFilms
                .stream()
                .filter(filmDTO -> filmDTO.getDateOfPremiere().after(Date.valueOf(today.toLocalDate())))
                .sorted(Comparator.comparing(FilmDTO::getDateOfPremiere))
                .toList();
        return filmReleasedSoon;
    }

    public List<FilmType> getFilmTypeListById(Long id) {
        Optional<Film> optionalFilm = getFilmById(id);
        List<FilmType> filmTypeList;

        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            filmTypeList = film.getFilmTypeList();
        } else {
            filmTypeList = new ArrayList<>();
        }
        return filmTypeList;
    }

    public void deleteFilmById(Long id) {
        Optional<Film> cinema = getFilmById(id);
        if (cinema.isPresent()) {
            filmRepo.delete(cinema.get());
            List<String> listImagesFileName = getListImagesFileNameById(id);
            uploadService.deleteUploadFiles(listImagesFileName);
        }
    }

    public List<String> getListImagesFileNameById(Long id) {
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", ""));
        Film film;
        Optional<Film> filmOptional = getFilmById(id);
        if (filmOptional.isPresent()) {
            film = filmOptional.get();

            fileNamesFromDB.set(0, film.getMainImage());
            fileNamesFromDB.set(1, film.getImage1());
            fileNamesFromDB.set(2, film.getImage2());
            fileNamesFromDB.set(3, film.getImage3());
            fileNamesFromDB.set(4, film.getImage4());
            fileNamesFromDB.set(5, film.getImage5());
        }
        return fileNamesFromDB;
    }

    public FilmDTO updateImagesOnModel(FilmDTO filmDTO, List<String> fileNamesFromDB) {
        filmDTO.setMainImage(fileNamesFromDB.get(0));
        filmDTO.setImage1(fileNamesFromDB.get(1));
        filmDTO.setImage2(fileNamesFromDB.get(2));
        filmDTO.setImage3(fileNamesFromDB.get(3));
        filmDTO.setImage4(fileNamesFromDB.get(4));
        filmDTO.setImage5(fileNamesFromDB.get(5));
        return filmDTO;
    }

    public void saveFilmDTO(FilmDTO filmDTOModel) {
        Optional<Film> filmOptional = getFilmById(filmDTOModel.getId());
        Film filmToSave;
        SeoBlock seoBlock;
        if (filmOptional.isPresent()) {
            filmToSave = filmOptional.get();
            seoBlock = filmToSave.getSeoBlock();
        } else {
            filmToSave = new Film();
            seoBlock = new SeoBlock();
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
        filmToSave.setDateOfPremiere(filmDTOModel.getDateOfPremiere());

        saveFilm(filmToSave);

    }
}
