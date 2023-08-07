package com.kino_cms.controller;

import com.kino_cms.entity.FeedPage;
import com.kino_cms.enums.FeedType;
import com.kino_cms.service.FeedPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PromotionPageController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    FeedPageService feedPageService;
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
        Optional<FeedPage> feedPageOptional = feedPageService.getFeedPageById(id);
        FeedPage feedPage;
        ArrayList<String> imageList;
        if (feedPageOptional.isPresent()) {
            feedPage = feedPageOptional.get();
            imageList = new ArrayList<>(6);
            imageList.add(feedPage.getMainImage());
            imageList.add(feedPage.getImage1());
            imageList.add(feedPage.getImage2());
            imageList.add(feedPage.getImage3());
            imageList.add(feedPage.getImage4());
            imageList.add(feedPage.getImage5());
        } else {
            feedPageModel.setId(id);
            feedPageModel.setFeedType(FeedType.PROMOTION);
            imageList = new ArrayList<>(List.of("", "", "", "", "", ""));
            feedPageModel.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }

        ArrayList<MultipartFile> images = new ArrayList<>();
        images.add(mainImage);
        images.add(image1);
        images.add(image2);
        images.add(image3);
        images.add(image4);
        images.add(image5);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            if (image.getOriginalFilename().equals("empty.png")) {
                File deleteImage = new File(uploadPath + "/" + imageList.get(i));
                deleteImage.delete();
                imageList.set(i, null);
            } else if (!image.isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                String fileName = uuidFile + "_" + image.getOriginalFilename();
                image.transferTo(new File(uploadPath + "/" + fileName));
                imageList.set(i, fileName);
            }
        }

        feedPageModel.setMainImage(imageList.get(0));
        feedPageModel.setImage1(imageList.get(1));
        feedPageModel.setImage2(imageList.get(2));
        feedPageModel.setImage3(imageList.get(3));
        feedPageModel.setImage4(imageList.get(4));
        feedPageModel.setImage5(imageList.get(5));

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