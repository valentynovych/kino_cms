package com.kino_cms.controller.admin;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.dto.Page;
import com.kino_cms.entity.ContactCinema;
import com.kino_cms.entity.ContactPage;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.HomePage;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import com.kino_cms.service.ContactPageService;
import com.kino_cms.service.GeneralPageService;
import com.kino_cms.service.HomePageService;
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
    GeneralPageService generalPageService;
    @Autowired
    PageService pageService;
    @Autowired
    HomePageService homePageService;
    @Autowired
    ContactPageService contactPageService;
    @Value("${upload.path}")
    private String uploadPath;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @GetMapping("/admin/view-pages")
    public String viewPages(Model model) {
        model.addAttribute("allPages", new ArrayList<>(getAllPages()));
        return "/admin/pages";
    }

    //edit page by type and id
    @GetMapping("/admin/edit-page/{pageType}/{id}")
    public String editPage(@PathVariable String id, @PathVariable String pageType) {
        switch (pageType) {
            case "HOME_PAGE":
                return "redirect:/admin/edit-homepage/" + id;
            case "CONTACT_PAGE":
                return "redirect:/admin/edit-contacts/" + id;
            case "ABOUT_CINEMA", "CHILD_ROOM", "ADVERTISING", "CAFE_BAR", "VIP_HALL", "OTHER_PAGE":
                return "redirect:/admin/edit-generalpage/" + id + "?pageType=" + pageType;
        }
        return "redirect:/admin/view-pages";
    }

    //delete page by type and id
    @GetMapping("admin/delete-page/{pageType}/{id}")
    public String deleteHomePage(@PathVariable String pageType, @PathVariable Long id) {
        switch (pageType) {
            case "HOME_PAGE", "CONTACT_PAGE", "ABOUT_CINEMA", "CAFE_BAR", "VIP_HALL", "ADVERTISING", "CHILD_ROOM":
                break;
            case "OTHER_PAGE":
                GeneralPage generalPage = generalPageService.findById(id).get();
                generalPageService.delete(generalPage);
                break;
        }
        return "redirect:/admin/view-pages";
    }

    //mapping for home page
    @GetMapping("/admin/edit-homepage/{id}")
    public String editHomePage(@PathVariable Long id, Model model,
                               @RequestParam(value = "language", required = false) Language language) {
        Optional<HomePage> optionalHomePage = homePageService.getHomePageByLanguageOrId(language, id);
        HomePage homePage;
        if (optionalHomePage.isPresent()) {
            homePage = optionalHomePage.get();
        } else {
            homePage = new HomePage();
            homePage.setPageType(PageType.HOME_PAGE);
            homePage.setLanguage(language);
            homePage.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }
        model.addAttribute("page", homePage);
        return "admin/pages/editHomePage";
    }

    @PostMapping("/admin/edit-homepage/{id}")
    public String saveHomePage(@ModelAttribute HomePage homePage) {
        homePageService.save(homePage);
        return "redirect:/admin/view-pages";
    }

    // mapping for general page
    @GetMapping("/admin/edit-generalpage/{id}")
    public String editGeneralPage(@PathVariable Long id, Model model,
                                  @RequestParam(required = false) String pageType,
                                  @RequestParam(required = false) Language language) {
        Optional<GeneralPageDTO> pageDTOByLanguagePageId = Optional.empty();
        Optional<GeneralPageDTO> pageDTOById = generalPageService.getGeneralPageDTOById(id);
        GeneralPageDTO generalPageDTO;
        if (language != null) {
            pageDTOByLanguagePageId = generalPageService.getGeneralPageDTOByLanguagePageId(id);
            if (pageDTOByLanguagePageId.isPresent()) {
                generalPageDTO = pageDTOById.get();
                generalPageDTO.setTranslatePageId(pageDTOByLanguagePageId.get().getId());
                generalPageService.saveGeneralPageDTO(generalPageDTO);
                return "redirect:/admin/edit-generalpage/" + generalPageDTO.getTranslatePageId() + "?pageType=" + pageType;
            }
        }

        if (pageDTOById.isPresent()) {
            generalPageDTO = pageDTOById.get();
            if (pageDTOByLanguagePageId.isPresent()) {
                GeneralPageDTO newGeneralPageDTO = pageDTOByLanguagePageId.get();
                generalPageDTO.setTranslatePageId(newGeneralPageDTO.getId());
                newGeneralPageDTO.setTranslatePageId(generalPageDTO.getId());
                generalPageService.saveGeneralPageDTO(generalPageDTO);
                generalPageDTO = newGeneralPageDTO;
            } else if (language != null && pageDTOByLanguagePageId.isEmpty()) {
                generalPageDTO = new GeneralPageDTO();
                generalPageDTO.setId(0L);
                generalPageDTO.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
                generalPageDTO.setPageType(PageType.valueOf(pageType));
                generalPageDTO.setTranslatePageId(id);
                generalPageDTO.setLanguage(language);
            } else {
                generalPageDTO = pageDTOById.get();
            }
            model.addAttribute("generalPage", generalPageDTO);
        } else {
            GeneralPageDTO newPage = new GeneralPageDTO();
            newPage.setId(0L);
            newPage.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
            newPage.setPageType(PageType.valueOf(pageType));
            if (language != null) {
                newPage.setTranslatePageId(id);
                newPage.setLanguage(language);
            } else {
                newPage.setLanguage(Language.UKRAINIAN);
            }
            model.addAttribute("generalPage", newPage);
        }
        return "admin/pages/editGeneralPage";
    }

    @PostMapping("/admin/edit-generalpage/{id}")
    public String saveGeneralPage(@ModelAttribute GeneralPageDTO generalPageModel,
                                  @PathVariable Long id,
                                  @RequestParam("mainImage1") MultipartFile mainImage,
                                  @RequestParam("image11") MultipartFile image1,
                                  @RequestParam("image21") MultipartFile image2,
                                  @RequestParam("image31") MultipartFile image3,
                                  @RequestParam("image41") MultipartFile image4,
                                  @RequestParam("image51") MultipartFile image5) throws IOException {
        Optional<GeneralPageDTO> optionalGeneralPage = generalPageService.getGeneralPageDTOById(generalPageModel.getId());
        GeneralPageDTO generalPage1;

        ArrayList<String> imageList;
        if (optionalGeneralPage.isPresent()) {
            generalPage1 = optionalGeneralPage.get();
            imageList = new ArrayList<>(6);
            imageList.add(generalPage1.getMainImage());
            imageList.add(generalPage1.getImage1());
            imageList.add(generalPage1.getImage2());
            imageList.add(generalPage1.getImage3());
            imageList.add(generalPage1.getImage4());
            imageList.add(generalPage1.getImage5());
        } else {
            generalPageModel.setId(0L);
            imageList = new ArrayList<>(List.of("", "", "", "", "", ""));
            generalPageModel.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
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

        generalPageModel.setMainImage(imageList.get(0));
        generalPageModel.setImage1(imageList.get(1));
        generalPageModel.setImage2(imageList.get(2));
        generalPageModel.setImage3(imageList.get(3));
        generalPageModel.setImage4(imageList.get(4));
        generalPageModel.setImage5(imageList.get(5));

        generalPageService.saveGeneralPageDTO(generalPageModel);
        return "redirect:/admin/view-pages";
    }

    //mapping for contact page
    @GetMapping("/admin/edit-contacts/{id}")
    public String editContactsPage(@PathVariable Long id, Model model,
                                   @RequestParam(required = false) String firstTitle) {

        Optional<ContactPage> optionalHomePage = contactPageService.getContactPage();
        ContactPage contactPage;

        if (optionalHomePage.isPresent()) {
            contactPage = optionalHomePage.get();
            if (contactPage.getContactCinemaList().isEmpty()) {
                ContactCinema contactCinema = new ContactCinema();
                contactCinema.setIsActivate(Boolean.TRUE);
                contactCinema.setContactPage(contactPage);
                contactPage.getContactCinemaList().add(contactCinema);
            }
        } else {
            contactPage = new ContactPage();
            contactPage.setTitle(firstTitle);
            contactPage.setPageType(PageType.HOME_PAGE);
            contactPage.setIsActive(Boolean.TRUE);
            contactPage.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }
        model.addAttribute("page", contactPage);
        return "admin/pages/editContacts";
    }

    @PostMapping("/admin/edit-contacts/{id}")
    public String saveContactsPage(@ModelAttribute ContactPage contactPageModel, @PathVariable Long id,
                                   @RequestParam("logo_image") MultipartFile[] logo_images) throws IOException {
        contactPageModel.setId(id);

        Optional<ContactPage> contactPageOptional = contactPageService.getContactPage();
        ContactPage contactPage1;
        ArrayList<String> imagesFromEntity;

        if (contactPageOptional.isPresent()) {
            contactPage1 = contactPageOptional.get();
            imagesFromEntity = new ArrayList<>();
            for (ContactCinema contactCinema : contactPage1.getContactCinemaList()) {
                imagesFromEntity.add(contactCinema.getLogo_image());
            }
        } else {
            contactPage1 = new ContactPage();
            contactPage1.setIsActive(Boolean.TRUE);
            imagesFromEntity = new ArrayList<>(1);
            contactPageModel.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
        }

        ArrayList<MultipartFile> imagesFromModel = new ArrayList<>();
        imagesFromModel.addAll(Arrays.asList(logo_images));

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        for (int i = 0; i < imagesFromModel.size(); i++) {
            MultipartFile image = imagesFromModel.get(i);
            if (Objects.equals(image.getOriginalFilename(), "empty.png")) {
                File deleteImage = new File(uploadPath + "/" + imagesFromEntity.get(i));
                deleteImage.delete();
                imagesFromEntity.set(i, null);
            } else if (!image.isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                String fileName = uuidFile + "_" + image.getOriginalFilename();
                image.transferTo(new File(uploadPath + "/" + fileName));
                imagesFromEntity.set(i, fileName);
            }
        }

        List<ContactCinema> contactCinemaList = contactPageModel.getContactCinemaList();
        for (int i = 0; i < imagesFromEntity.size(); i++) {
            ContactCinema contactCinema = contactCinemaList.get(i);
            contactCinema.setLogo_image(imagesFromEntity.get(i));
            contactCinemaList.set(i, contactCinema);
        }
        contactPage1.setContactCinemaList(contactCinemaList);
        contactPage1.setSeoBlock(contactPageModel.getSeoBlock());
        contactPageModel.setContactCinemaList(contactCinemaList);

        contactPageService.save(contactPage1);
        return "redirect:/admin/view-pages";
    }

    public List<Page> getAllPages() {
        return pageService.getAllPages();
    }
}
