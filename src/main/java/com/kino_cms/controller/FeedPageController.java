package com.kino_cms.controller;


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
public class FeedPageController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    FeedPageService feedPageService;
    @Autowired
    SaveUploadService uploadService;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @GetMapping("/admin/edit-feed/{id}")
    public String editFeedPage(@PathVariable Long id, Model model) {
        Optional<FeedPage> optionalFeedPage = feedPageService.getFeedPageById(id);

        FeedPage feedPageFromDB;
        if (optionalFeedPage.isPresent()) {
            feedPageFromDB = optionalFeedPage.get();
            model.addAttribute("feedPage", feedPageFromDB);
        } else {
            feedPageFromDB = new FeedPage();
            feedPageFromDB.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
            feedPageFromDB.setFeedType(FeedType.FEED);
            model.addAttribute("feedPage", feedPageFromDB);
        }
        return "admin/feedAndPromotion/feedPage";
    }

    @PostMapping("/admin/edit-feed/{id}")
    public String saveFeedPage(@ModelAttribute FeedPage feedPageModel,
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
            feedPageModel.setFeedType(FeedType.FEED);
            fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", ""));
            feedPageModel.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }

        fileNamesFromDB = uploadService.saveUploadFiles(images, fileNamesFromDB);
        feedPageModel = feedPageService.updateImagesOnModel(feedPageModel, id, fileNamesFromDB);

        feedPageService.saveFeedPage(feedPageModel);
        return "redirect:/admin/view-feeds";
    }

    @GetMapping("/admin/view-feeds")
    public String viewFeeds(Model model) {
        List<FeedPage> feedPageList = feedPageService.findFeedPagesByFeedType(FeedType.FEED);
        model.addAttribute("feedPages", feedPageList);
        return "admin/feedAndPromotion/viewFeeds";
    }

    @GetMapping("/admin/delete-feed/{id}")
    public String deleteFeed(@PathVariable Long id) {
        Optional<FeedPage> feedPageOptional = feedPageService.getFeedPageById(id);
        if (feedPageOptional.isPresent()) {
            feedPageService.deleteFeedPage(feedPageOptional.get());
        }
        return "redirect:/admin/view-feeds";
    }
}
