package com.kino_cms.controller;

import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.HomePage;
import com.kino_cms.entity.Page;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public static String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

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
            case "ABOUT_CINEMA":
                return "redirect:/admin/edit-aboutcinema/" + id;
            case "CAFE_BAR":

            case "VIP_HALL":

            case "ADVERTISING":

            case "CHILD_ROOM":

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
            case "ABOUT_CINEMA":

            case "CAFE_BAR":

            case "VIP_HALL":

            case "ADVERTISING":

            case "CHILD_ROOM":

        }
        return "redirect:/admin/view-pages";
    }

    //mapping for home page
    @GetMapping("/admin/edit-homepage/{id}")
    public String editHomePage(@PathVariable Long id, Model model) {

        HomePage homePage = homePageRepo.findById(id).get();
        model.addAttribute("page", homePage);
        return "admin/editPage/editHomePage";
    }

    @PostMapping("/admin/edit-homepage/{id}")
    public String saveHomePage(@ModelAttribute HomePage homePage, @PathVariable Long id) {
        homePage.setId(id);
        homePageRepo.save(homePage);
        return "redirect:/admin/view-pages";
    }

    // mapping for about cinema page
    @GetMapping("/admin/edit-aboutcinema/{id}")
    public String editAboutCinema(@PathVariable Long id, Model model) {
        Optional<GeneralPage> generalPage = pageRepo.findById(id);
        if (generalPage.isPresent()){
            model.addAttribute("aboutPage", generalPage.get());
        } else {
            model.addAttribute("aboutPage", new GeneralPage());
        }
        return "admin/editPage/editAboutCinemaPage";
    }

    @PostMapping("/admin/edit-aboutcinema/{id}")
    public String saveAboutCinema(@ModelAttribute GeneralPage generalPage,
                                  @PathVariable Long id,
                                  @RequestParam("mainImage1")MultipartFile file) throws IOException {
        generalPage.setId(id);

//        StringBuilder fileName = new StringBuilder();
//        Path fileNameAndPath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
//
//        fileName.append(file.getOriginalFilename());
//        Files.write(fileNameAndPath, file.getBytes());
        if (file != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString().toString();
            String fileName = uuidFile + "_" + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + fileName));
            generalPage.setMainImage(fileName);
        }
//        generalPage.setMainImage(fileNameAndPath.toString());
        pageRepo.save(generalPage);
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
