package com.kino_cms.controller;

import com.kino_cms.dto.Page;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.HomePage;
import com.kino_cms.enums.PageType;
import com.kino_cms.repository.GeneralPageRepo;
import com.kino_cms.repository.HomePageRepo;
import com.kino_cms.service.PageService;
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
import java.util.*;

@Controller
public class PageController {

    @Autowired
    GeneralPageRepo pageRepo;
    @Autowired
    PageService pageService;
    @Autowired
    HomePageRepo homePageRepo;
    @Value("${upload.path}")
    private String uploadPath;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @GetMapping("/admin/add-page")
    public String addPage(Model model) {
        model.addAttribute("pageTypes", Arrays.stream(PageType.values()).toArray());
        Page page = new GeneralPage();
        model.addAttribute("page", page);
        return "/admin/editPage/addPage";
    }

    @PostMapping("/admin/add-page")
    public String pageSelect(@ModelAttribute GeneralPage page) {
        String pageType = page.getPageType().name();
        page.setId(0L);
        switch (pageType) {
            case "HOME_PAGE":
                return "redirect:/admin/edit-homepage/" + page.getId() + "?title=" + page.getTitle();
            case "CONTACT_PAGE":
                return "redirect:/admin/edit-contactpage/" + page.getId() + "?title=" + page.getTitle();
            case "ABOUT_CINEMA", "CHILD_ROOM", "ADVERTISING", "CAFE_BAR", "VIP_HALL":
                return "redirect:/admin/edit-generalpage/" + page.getId() + "?title=" + page.getTitle()
                        + "&pageType=" + pageType;
        }
        return "redirect:/admin/view-pages";
    }


    @GetMapping("/admin/view-pages")
    public String viewPages(Model model) {
        model.addAttribute("pages", getAllPages());
        return "/admin/pages";
    }

    //edit page by type and id
    @GetMapping("/admin/edit-page/{pageType}/{id}")
    public String editPage(@PathVariable String id, @PathVariable String pageType) {
        switch (pageType) {
            case "HOME_PAGE":
                return "redirect:/admin/edit-homepage/" + id;
            case "CONTACT_PAGE":
                return "redirect:/admin/edit-contactpage/" + id;
            case "ABOUT_CINEMA", "CHILD_ROOM", "ADVERTISING", "CAFE_BAR", "VIP_HALL", "OTHER_PAGE":
                return "redirect:/admin/edit-generalpage/" + id;
        }
        return "redirect:/admin/view-pages";
    }

    //delete page by type and id
    @GetMapping("admin/delete-page/{pageType}/{id}")
    public String deleteHomePage(@PathVariable String pageType, @PathVariable Long id) {
        switch (pageType) {
            case "HOME_PAGE":
                HomePage homePage = homePageRepo.findById(id).get();
                homePageRepo.delete(homePage);
                break;
            case "CONTACT_PAGE":
                break;
            case "ABOUT_CINEMA", "CAFE_BAR", "VIP_HALL", "ADVERTISING", "CHILD_ROOM", "OTHER_PAGE":
                GeneralPage generalPage = pageRepo.findById(id).get();
                pageRepo.delete(generalPage);
                break;
        }
        return "redirect:/admin/view-pages";
    }

    //mapping for home page
    @GetMapping("/admin/edit-homepage/{id}")
    public String editHomePage(@PathVariable Long id, Model model, @RequestParam(required = false) String title) {

        Optional<HomePage> optionalHomePage = homePageRepo.findById(id);
        if (optionalHomePage.isPresent()) {
            HomePage homePage = optionalHomePage.get();

            model.addAttribute("page", homePage);
        } else {
            HomePage homePage = new HomePage();
            homePage.setTitle(title);
            homePage.setPageType(PageType.HOME_PAGE);
            homePage.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
            model.addAttribute("page", homePage);
        }
        return "admin/editPage/editHomePage";
    }

    @PostMapping("/admin/edit-homepage/{id}")
    public String saveHomePage(@ModelAttribute HomePage homePage, @PathVariable Long id) {
        homePage.setId(id);
        homePageRepo.save(homePage);
        return "redirect:/admin/view-pages";
    }

    // mapping for general page
    @GetMapping("/admin/edit-generalpage/{id}")
    public String editGeneralPage(@PathVariable Long id, Model model,
                                  @RequestParam(required = false) String title,
                                  @RequestParam(required = false) String pageType) {
        Optional<GeneralPage> generalPage = pageRepo.findById(id);

        if (generalPage.isPresent()) {
            model.addAttribute("generalPage", generalPage.get());
        } else {
            GeneralPage newPage = new GeneralPage();
            newPage.setTitle(title);
            newPage.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
            newPage.setPageType(PageType.valueOf(pageType));
            model.addAttribute("generalPage", newPage);
        }
        return "admin/editPage/editGeneralPage";
    }

    @PostMapping("/admin/edit-generalpage/{id}")
    public String saveGeneralPage(@ModelAttribute GeneralPage generalPageModel,
                                  @PathVariable Long id,
                                  @RequestParam("mainImage1") MultipartFile mainImage,
                                  @RequestParam("image11") MultipartFile image1,
                                  @RequestParam("image21") MultipartFile image2,
                                  @RequestParam("image31") MultipartFile image3,
                                  @RequestParam("image41") MultipartFile image4,
                                  @RequestParam("image51") MultipartFile image5) throws IOException {
        Optional<GeneralPage> optionalGeneralPage = pageRepo.findById(id);
        GeneralPage generalPage1;
        ArrayList<String> imageList = new ArrayList<>();
        if (optionalGeneralPage.isPresent()) {
            generalPage1 = optionalGeneralPage.get();
            imageList.add(generalPage1.getMainImage());
            imageList.add(generalPage1.getImage1());
            imageList.add(generalPage1.getImage2());
            imageList.add(generalPage1.getImage3());
            imageList.add(generalPage1.getImage4());
            imageList.add(generalPage1.getImage5());

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
        for (int i = 0; i < images.size(); i++){
            MultipartFile image = images.get(i);
            if (image.getOriginalFilename().equals("empty.png")){
                imageList.add(i, null);
            } else if (!image.isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                String fileName = uuidFile + "_" + image.getOriginalFilename();
                image.transferTo(new File(uploadPath + "/" + fileName));
                imageList.set(i, fileName);
            }
        }

        generalPageModel.setMainImage(imageList.get(0));
        generalPageModel.setImage1(imageList.get(1));
        generalPageModel.setImage2(imageList.get(2));
        generalPageModel.setImage3(imageList.get(3));
        generalPageModel.setImage4(imageList.get(4));
        generalPageModel.setImage5(imageList.get(5));
//        for (MultipartFile image : images) {
//            if (!image.isEmpty()) {
//                String uuidFile = UUID.randomUUID().toString();
//                String fileName = uuidFile + "_" + image.getOriginalFilename();
//                image.transferTo(new File(uploadPath + "/" + fileName));
//                if (image.equals(mainImage)) {
//                    generalPageModel.setMainImage(fileName);
//                } else if (image.equals(image1)) {
//                    generalPageModel.setImage1(fileName);
//                } else if (image.equals(image2)) {
//                    generalPageModel.setImage2(fileName);
//                } else if (image.equals(image3)) {
//                    generalPageModel.setImage3(fileName);
//                } else if (image.equals(image4)) {
//                    generalPageModel.setImage4(fileName);
//                } else if (image.equals(image5)) {
//                    generalPageModel.setImage5(fileName);
//                }
//            }
//        }

        pageRepo.save(generalPageModel);
        return "redirect:/admin/view-pages";
    }

    // model attribute
    @ModelAttribute("getGeneralPages")
    public List<GeneralPage> getGeneralPages() {
        return pageRepo.findAll();
    }

    public List<Page> getAllPages() {
        return pageService.getAllPages();
    }
}
