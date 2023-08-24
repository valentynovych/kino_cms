package com.kino_cms.controller.admin;

import com.kino_cms.entity.FeedPage;
import com.kino_cms.enums.FeedType;
import com.kino_cms.service.FeedPageService;
import com.kino_cms.service.SaveUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class PromotionPageController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    FeedPageService feedPageService;
    @Autowired
    SaveUploadService uploadService;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @GetMapping("/admin/edit-promotion/{id}")
    public String editPromotionPage(@PathVariable Long id, Model model) {
        Optional<FeedPage> optionalFeedPage = feedPageService.getFeedPageById(id);

        FeedPage feedPageFromDB;
        if (optionalFeedPage.isPresent()) {
            feedPageFromDB = optionalFeedPage.get();
            model.addAttribute("feedPage", feedPageFromDB);
        } else {
            feedPageFromDB = new FeedPage();
            feedPageFromDB.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
            feedPageFromDB.setFeedType(FeedType.PROMOTION);
            model.addAttribute("feedPage", feedPageFromDB);
        }
        return "admin/feedAndPromotion/promotionPage";
    }

    @PostMapping("/admin/edit-promotion/{id}")
    public String savePromotionPage(@ModelAttribute FeedPage feedPageModel,
                                    @PathVariable Long id,
                                    @RequestParam("mainImage1") MultipartFile mainImage,
                                    @RequestParam("image11") MultipartFile image1,
                                    @RequestParam("image21") MultipartFile image2,
                                    @RequestParam("image31") MultipartFile image3,
                                    @RequestParam("image41") MultipartFile image4,
                                    @RequestParam("image51") MultipartFile image5) throws IOException {
        ArrayList<MultipartFile> images = new ArrayList<>(List.of(mainImage, image1, image2, image3, image4, image5));
        Optional<FeedPage> feedPageOptional = feedPageService.getFeedPageById(id);
        List<String> fileNamesFromDB;
        if (feedPageOptional.isPresent()) {
            fileNamesFromDB = feedPageService.getListImagesFileNameById(id);
        } else {
            feedPageModel.setId(id);
            feedPageModel.setFeedType(FeedType.PROMOTION);
            fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", ""));
            feedPageModel.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }

        fileNamesFromDB = uploadService.saveUploadFiles(images, fileNamesFromDB);
        feedPageModel = feedPageService.updateImagesOnModel(feedPageModel, id, fileNamesFromDB);

        feedPageService.saveFeedPage(feedPageModel);
        return "redirect:/admin/view-promotions";
    }

    @GetMapping("/admin/view-promotions")
    public String viewPromotions(Model model) {
        List<FeedPage> feedPageList = feedPageService.findFeedPagesByFeedType(FeedType.PROMOTION);
        model.addAttribute("feedPages", feedPageList);
        return "admin/feedAndPromotion/viewPromotions";
    }

    @GetMapping("/admin/delete-promotion/{id}")
    public String deletePromotion(@PathVariable Long id) {
        Optional<FeedPage> feedPageOptional = feedPageService.getFeedPageById(id);
        if (feedPageOptional.isPresent()) {
            feedPageService.deleteFeedPage(feedPageOptional.get());
        }
        return "redirect:/admin/view-promotions";
    }
}