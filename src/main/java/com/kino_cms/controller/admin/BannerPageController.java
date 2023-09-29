package com.kino_cms.controller.admin;

import com.kino_cms.dto.BannerDTO;
import com.kino_cms.enums.BannerType;
import com.kino_cms.service.BannerService;
import com.kino_cms.utils.SaveUploadFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class BannerPageController {
    @Autowired
    BannerService bannerService;
    @Autowired
    SaveUploadFileUtils uploadService;

    @GetMapping("/admin/edit-banners")
    public String editBanner(Model model) {
        model.addAttribute("headerBanner", bannerService.getHeaderBanner());
        model.addAttribute("perforatingBanner", bannerService.getPerforatingBanner());
        model.addAttribute("promotionBanner", bannerService.getPromotionBanner());
        List<Integer> seconds = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        model.addAttribute("seconds", seconds);
        return "admin/banners/editBanners";
    }

    @PostMapping("/admin/save-header-banner")
    public String saveHeaderBanner(@ModelAttribute("headerBanner") BannerDTO bannerDTOModel,
                                   @RequestParam("headerBannerImg") MultipartFile[] headerBannerImg) throws IOException {
        ArrayList<MultipartFile> images = new ArrayList<>(Arrays.stream(headerBannerImg).toList());
        List<String> fileNamesFromDB = bannerService.getListImagesFileNameByBannerType(BannerType.HEADER);
        bannerService.deleteEmptyBannerImage(images, bannerDTOModel);
        fileNamesFromDB = uploadService.saveUploadFiles(images, fileNamesFromDB);
        bannerDTOModel = bannerService.updateImagesOnModel(bannerDTOModel, fileNamesFromDB);

        bannerService.saveBannerDTO(bannerDTOModel);
        return "redirect:/admin/edit-banners";
    }

    @PostMapping("/admin/save-perforating-banner")
    public String savePerforatingBanner(@ModelAttribute("perforatingBanner") BannerDTO bannerDTOModel,
                                        @RequestParam("perforatingBannerImg") MultipartFile perforatingBannerImg) throws IOException {
        ArrayList<MultipartFile> images = new ArrayList<>();
        images.add(perforatingBannerImg);
        List<String> fileNamesFromDB = bannerService.getListImagesFileNameByBannerType(BannerType.PERFORATING);
        BannerDTO dto = bannerService.getPerforatingBanner();
        bannerDTOModel.setBannerImages(dto.getBannerImages());
        fileNamesFromDB = uploadService.saveUploadFiles(images, fileNamesFromDB);
        bannerDTOModel = bannerService.updateImagesOnModel(bannerDTOModel, fileNamesFromDB);

        bannerService.saveBannerDTO(bannerDTOModel);
        return "redirect:/admin/edit-banners";
    }

    @PostMapping("/admin/save-promotion-banner")
    public String savePromotionBanner(@ModelAttribute("promotionBanner") BannerDTO bannerDTOModel,
                                      @RequestParam("promotionBannerImg") MultipartFile[] promotionBannerImg) throws IOException {
        ArrayList<MultipartFile> images = new ArrayList<>(Arrays.stream(promotionBannerImg).toList());
        List<String> fileNamesFromDB = bannerService.getListImagesFileNameByBannerType(BannerType.PROMOTION);
        bannerService.deleteEmptyBannerImage(images, bannerDTOModel);
        fileNamesFromDB = uploadService.saveUploadFiles(images, fileNamesFromDB);
        bannerDTOModel = bannerService.updateImagesOnModel(bannerDTOModel, fileNamesFromDB);

        bannerService.saveBannerDTO(bannerDTOModel);
        return "redirect:/admin/edit-banners";
    }
}
