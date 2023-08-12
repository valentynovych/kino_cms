package com.kino_cms.controller;

import com.kino_cms.dto.HallDTO;
import com.kino_cms.service.HallService;
import com.kino_cms.service.SaveUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HallPageController {
    @Autowired
    HallService hallService;
    @Autowired
    SaveUploadService uploadService;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @GetMapping("/admin/edit-hall/{id}/{cinema}")
    public String editHall(@PathVariable Long id,
                           @PathVariable Long cinema,
                           Model model) {
        Optional<HallDTO> optionalFeedPage = hallService.getHallDtoById(id);

        HallDTO hallDTO;
        if (optionalFeedPage.isPresent()) {
            hallDTO = optionalFeedPage.get();
        } else {
            hallDTO = new HallDTO();
            hallDTO.setCinemaId(cinema);
            hallDTO.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }
        model.addAttribute("hallDTO", hallDTO);
        return "admin/cinemas/editHall";
    }

    @PostMapping("/admin/edit-hall/{id}/{cinema}")
    public String saveHall(@ModelAttribute HallDTO hallDTOModel,
                           @PathVariable Long id,
                           @RequestParam("hallSchema1") MultipartFile hallSchema,
                           @RequestParam("firstBanner1") MultipartFile firstBanner,
                           @RequestParam("image11") MultipartFile image1,
                           @RequestParam("image21") MultipartFile image2,
                           @RequestParam("image31") MultipartFile image3,
                           @RequestParam("image41") MultipartFile image4,
                           @RequestParam("image51") MultipartFile image5) throws IOException {

        ArrayList<MultipartFile> images = new ArrayList<>(
                List.of(hallSchema, firstBanner, image1, image2, image3, image4, image5));
        Optional<HallDTO> hallOptional = hallService.getHallDtoById(id);
        List<String> fileNamesFromDB;
        if (hallOptional.isPresent()) {
            fileNamesFromDB = hallService.getListImagesFileNameById(id);
        } else {
            hallDTOModel.setId(id);
            fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", "", ""));
            hallDTOModel.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }

        fileNamesFromDB = uploadService.saveUploadFiles(images, fileNamesFromDB);
        hallDTOModel = hallService.updateImagesOnModel(hallDTOModel, fileNamesFromDB);

        hallService.saveHallDTO(hallDTOModel);
        return "redirect:/admin/edit-cinema/" + hallDTOModel.getCinemaId();
    }

    @GetMapping("/admin/delete-hall/{id}")
    public String deleteHall(@PathVariable Long id) {
        Long cinemaId = hallService.getCinemaIdByHall(id);
        hallService.deleteHall(id);
        return "redirect:/admin/edit-cinema/" + cinemaId;
    }

}
