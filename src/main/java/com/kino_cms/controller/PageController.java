package com.kino_cms.controller;

import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.HomePage;
import com.kino_cms.entity.Page;
import com.kino_cms.enums.PageType;
import com.kino_cms.repository.GeneralPageRepo;
import com.kino_cms.repository.HomePageRepo;
import com.kino_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    GeneralPageRepo pageRepo;
    @Autowired
    PageService pageService;
    @Autowired
    HomePageRepo homePageRepo;

    @GetMapping("/admin/view-pages")
    public String viewPages(Model model) {
        model.addAttribute("pages", getAllPages());
        return "/admin/pages";
    }

    @GetMapping("/admin/edit-page/{pageType}/{id}")
    public String editPage(@PathVariable String id, @PathVariable String pageType) {
        switch (pageType) {
            case "HOME_PAGE":
                return "redirect:/admin/edit-homepage/" + id;
        }
        return "";
    }

    @GetMapping("/admin/edit-homepage/{id}")
    public String editPage(@PathVariable Long id, Model model) {

        HomePage homePage = homePageRepo.findById(id).get();
        model.addAttribute("page", homePage);
        return "admin/editPage/editHomePage";
    }

    @ModelAttribute("getGeneralPages")
    public List<GeneralPage> getGeneralPages() {
        return pageRepo.findAll();
    }

    public List<Page> getAllPages() {
        return pageService.getAllPages();
    }
}
