package com.kino_cms.controller.admin;

import com.kino_cms.dto.HallDTO;
import com.kino_cms.enums.Language;
import com.kino_cms.service.HallService;
import com.kino_cms.utils.SaveUploadFileUtils;
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
    SaveUploadFileUtils uploadService;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @GetMapping("/admin/edit-hall/{id}/{cinema}")
    public String editHall(@PathVariable Long id,
                           @PathVariable Long cinema,
                           @RequestParam(required = false) Language language,
                           Model model) {
        Optional<HallDTO> optionalHallDTO = hallService.getHallDtoById(id);
        Optional<HallDTO> optionalHallDTOByTranslate = Optional.empty();
        if (language != null) {
            optionalHallDTOByTranslate = hallService.getHallDtoByTranslatePageId(id);
            if (optionalHallDTOByTranslate.isPresent()) {
                return "redirect:/admin/edit-hall/" +
                        optionalHallDTOByTranslate.get().getId() +
                        "/" + optionalHallDTOByTranslate.get().getCinemaId();
            }
        }
        HallDTO hallDTO;
        if (optionalHallDTO.isPresent()) {
            hallDTO = optionalHallDTO.get();
            if (language != null && optionalHallDTOByTranslate.isEmpty()) {
                hallDTO = new HallDTO();
                hallDTO.setId(0L);
                hallDTO.setTranslatePageId(id);
                hallDTO.setLanguage(language);
            }
        } else {
            hallDTO = new HallDTO();
            hallDTO.setId(0L);
            hallDTO.setCinemaId(cinema);
            hallDTO.setLanguage(Language.UKRAINIAN);
            hallDTO.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }
        model.addAttribute("hallDTO", hallDTO);
        return "admin/cinemas/editHall";
    }

    @PostMapping("/admin/edit-hall/{id}/{cinema}")
    public String saveHall(@ModelAttribute HallDTO hallDTOModel,
                           @PathVariable Long id,
                           @PathVariable Long cinema,
                           @RequestParam("hallSchema1") MultipartFile hallSchema,
                           @RequestParam("firstBanner1") MultipartFile firstBanner,
                           @RequestParam("image11") MultipartFile image1,
                           @RequestParam("image21") MultipartFile image2,
                           @RequestParam("image31") MultipartFile image3,
                           @RequestParam("image41") MultipartFile image4,
                           @RequestParam("image51") MultipartFile image5) throws IOException {

        ArrayList<MultipartFile> images = new ArrayList<>(
                List.of(hallSchema, firstBanner, image1, image2, image3, image4, image5));
        Optional<HallDTO> hallOptional = hallService.getHallDtoById(hallDTOModel.getId());
        List<String> fileNamesFromDB;
        if (hallOptional.isPresent()) {
            fileNamesFromDB = hallService.getListImagesFileNameById(hallDTOModel.getId());
        } else {
            hallDTOModel.setId(hallDTOModel.getId());
            hallDTOModel.setCinemaId(cinema);
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
