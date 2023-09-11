package com.kino_cms.controller.admin;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.enums.FilmType;
import com.kino_cms.enums.Language;
import com.kino_cms.service.FilmService;
import com.kino_cms.service.SaveUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FilmPageController {
    @Autowired
    FilmService filmService;
    @Autowired
    SaveUploadService uploadService;

    @GetMapping("/admin/edit-film/{id}")
    public String editFilm(@PathVariable Long id,
                           @RequestParam(required = false) Language language,
                           Model model) {
        List<FilmType> filmTypeList = List.of(FilmType.D3, FilmType.D2, FilmType.IMAX);
        Optional<FilmDTO> optionalFeedPage = filmService.getFilmDtoById(id);
        List<FilmType> typesOnDto = filmService.getFilmTypeListById(id);

        Optional<FilmDTO> filmDTOTranslatePage = Optional.empty();
        if (language != null) {
            filmDTOTranslatePage = filmService.getFilmDtoByTranslatePageId(id);
            if (filmDTOTranslatePage.isPresent()) {
                return "redirect:/admin/edit-film/" + filmDTOTranslatePage.get().getId();
            }
        }

        FilmDTO filmDTO;
        if (optionalFeedPage.isPresent()) {
            filmDTO = optionalFeedPage.get();
            if (language != null && filmDTOTranslatePage.isEmpty()) {
                filmDTO = new FilmDTO();
                filmDTO.setId(0L);
                filmDTO.setTranslatePageId(id);
                filmDTO.setLanguage(language);
            }
        } else {
            filmDTO = new FilmDTO();
            filmDTO.setId(0L);
            filmDTO.setLanguage(Language.UKRAINIAN);
        }

        filmDTO.setFilmTypeList(typesOnDto);
        model.addAttribute("filmDTO", filmDTO);
        model.addAttribute("filmTypes", filmTypeList);
        return "admin/films/editFilm";
    }

    @PostMapping("/admin/edit-film/{id}")
    public String saveFilm(@ModelAttribute FilmDTO filmDTOModel,
                           @PathVariable Long id,
                           @RequestParam("mainImage1") MultipartFile mainImage,
                           @RequestParam("image11") MultipartFile image1,
                           @RequestParam("image21") MultipartFile image2,
                           @RequestParam("image31") MultipartFile image3,
                           @RequestParam("image41") MultipartFile image4,
                           @RequestParam("image51") MultipartFile image5) throws IOException {

        ArrayList<MultipartFile> images = new ArrayList<>(
                List.of(mainImage, image1, image2, image3, image4, image5));
        Optional<FilmDTO> filmDTOOptional = filmService.getFilmDtoById(filmDTOModel.getId());
        List<String> fileNamesFromDB;
        if (filmDTOOptional.isPresent()) {
            fileNamesFromDB = filmService.getListImagesFileNameById(filmDTOModel.getId());
        } else {
            filmDTOModel.setId(filmDTOModel.getId());
            fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", ""));
        }

        fileNamesFromDB = uploadService.saveUploadFiles(images, fileNamesFromDB);
        filmDTOModel = filmService.updateImagesOnModel(filmDTOModel, fileNamesFromDB);

        filmService.saveFilmDTO(filmDTOModel);
        return "redirect:/admin/view-films";
    }

    @GetMapping("/admin/view-films")
    public String viewFilms(Model model) {
        List<FilmDTO> filmIsReleasedNow = filmService.getAllFilmIsReleasedNow();
        List<FilmDTO> filmReleasedSoon = filmService.getAllFilmReleasedSoon();

        model.addAttribute("filmIsReleasedNow", filmIsReleasedNow);
        model.addAttribute("filmReleasedSoon", filmReleasedSoon);
        return "admin/films/viewFilms";
    }
}
