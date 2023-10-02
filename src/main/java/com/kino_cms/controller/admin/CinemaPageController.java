package com.kino_cms.controller.admin;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.dto.HallDTO;
import com.kino_cms.enums.Language;
import com.kino_cms.service.CinemaService;
import com.kino_cms.service.HallService;
import com.kino_cms.utils.SaveUploadFileUtils;
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
public class CinemaPageController {
    @Autowired
    CinemaService cinemaService;
    @Autowired
    HallService hallService;
    @Autowired
    SaveUploadFileUtils uploadService;

    @GetMapping("/admin/edit-cinema/{id}")
    public String editCinema(@PathVariable Long id,
                             @RequestParam(required = false) Language language,
                             Model model) {
        Optional<CinemaDTO> optionalCinemaDTO = cinemaService.getCinemaDtoById(id);
        Optional<CinemaDTO> cinemaByTranslatePageId = Optional.empty();
        CinemaDTO cinemaDTO;
        ArrayList<HallDTO> hallDTOList;

        if (language != null) {
            cinemaByTranslatePageId = cinemaService.getCinemaByTranslatePageId(id);
            if (cinemaByTranslatePageId.isPresent()) {
                return "redirect:/admin/edit-cinema/" + cinemaByTranslatePageId.get().getId();
            }
        }
        if (optionalCinemaDTO.isPresent()) {
            cinemaDTO = optionalCinemaDTO.get();
            if (language != null && cinemaByTranslatePageId.isEmpty()) {
                cinemaDTO = new CinemaDTO();
                cinemaDTO.setId(0L);
                cinemaDTO.setTranslatePageId(id);
                cinemaDTO.setLanguage(language);
            }
            hallDTOList = (ArrayList<HallDTO>) hallService.getAllHallByCinema(cinemaDTO);
            if (!hallDTOList.isEmpty()) {
                cinemaDTO.setHallDTOList(hallDTOList);
            } else {
                HallDTO hallDTO = new HallDTO();
                hallDTO.setId(0L);
                hallDTO.setName("Новий зал");
                hallDTOList.add(hallDTO);
                cinemaDTO.setHallDTOList(hallDTOList);
            }
        } else {
            cinemaDTO = new CinemaDTO();
            if (language != null) {
                cinemaDTO.setLanguage(language);
                cinemaDTO.setTranslatePageId(id);
            } else {
                cinemaDTO.setLanguage(Language.UKRAINIAN);
            }
            cinemaDTO.setId(0L);
            hallDTOList = new ArrayList<>();
            HallDTO hallDTO = new HallDTO();
            hallDTO.setId(0L);
            hallDTO.setName("Новий зал");
            hallDTOList.add(hallDTO);
            cinemaDTO.setHallDTOList(hallDTOList);
        }

        model.addAttribute("cinema", cinemaDTO);
        return "admin/cinemas/editCinema";
    }

    @PostMapping("/admin/edit-cinema/{id}")
    public String saveHall(@ModelAttribute CinemaDTO cinemaDTOModel,
                           @PathVariable Long id,
                           @RequestParam("logoImage1") MultipartFile logoImage,
                           @RequestParam("firstBanner1") MultipartFile firstBanner,
                           @RequestParam("image11") MultipartFile image1,
                           @RequestParam("image21") MultipartFile image2,
                           @RequestParam("image31") MultipartFile image3,
                           @RequestParam("image41") MultipartFile image4,
                           @RequestParam("image51") MultipartFile image5) throws IOException {

        ArrayList<MultipartFile> images = new ArrayList<>(
                List.of(logoImage, firstBanner, image1, image2, image3, image4, image5));
        Optional<CinemaDTO> cinemaDTOOptional = cinemaService.getCinemaDtoById(cinemaDTOModel.getId());
        List<String> fileNamesFromDB;
        if (cinemaDTOOptional.isPresent()) {
            fileNamesFromDB = cinemaService.getListImagesFileNameById(id);
        } else {
            cinemaDTOModel.setId(cinemaDTOModel.getId());
            fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", "", ""));

        }

        fileNamesFromDB = uploadService.saveUploadFiles(images, fileNamesFromDB);
        cinemaDTOModel = cinemaService.updateImagesOnModel(cinemaDTOModel, fileNamesFromDB);

        cinemaService.saveCinemaDto(cinemaDTOModel);
        return "redirect:/admin/view-cinemas";
    }

    @GetMapping("/admin/delete-cinema/{id}")
    public String deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinemaById(id);
        return "redirect:/admin/view-cinemas";
    }

    @GetMapping("/admin/view-cinemas")
    public String viewCinemas(Model model) {
        List<CinemaDTO> cinemaDTOList = cinemaService.getAllCinemaDto();
        if (cinemaDTOList.isEmpty()) {
            cinemaDTOList.add(new CinemaDTO());
        }
        model.addAttribute("cinemaList", cinemaDTOList);
        return "admin/cinemas/viewCinemas";
    }
}
