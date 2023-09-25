package com.kino_cms;

import com.kino_cms.entity.*;
import com.kino_cms.enums.City;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import com.kino_cms.enums.Role;
import com.kino_cms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final HomePageRepo homePageRepo;
    private final GeneralPageRepo generalPageRepo;
    private final ContactPageRepo contactPageRepo;
    private final CinemaRepo cinemaRepo;
    private final static String datePattern = "yyyy-MM-dd HH:mm";

    @Override
    public void run(String... args) throws Exception {
        log.info("start runner");
        createAndSaveDefaultEntity();
    }

    private void createAndSaveDefaultEntity() {
        if (userRepository.count() == 0) {
            log.info("create default users");
            userRepository.saveAll(createDefaultUsers());
        }
        if (homePageRepo.count() == 0) {
            homePageRepo.save(createDefaultHomePage());
        }
        if (generalPageRepo.count() < 5) {
            generalPageRepo.saveAll(createDefaultGeneralPage());
        }
        if (contactPageRepo.count() == 0) {
            contactPageRepo.save(createDefaultContactPage());
        }
        if (cinemaRepo.count() == 0) {
            cinemaRepo.save(createDefaultCinema());
        }
    }

    private List<User> createDefaultUsers() {

        User admin = new User();
        admin.setRole(Role.ROLE_ADMIN);
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@gmail.com");
        admin.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        UserDetails adminDetails = new UserDetails();
        adminDetails.setLanguage(Language.UKRAINIAN);
        adminDetails.setCity(City.KYIV);
        adminDetails.setSex("Male");
        admin.setUserDetails(adminDetails);

        User user = new User();
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode("user"));
        user.setEmail("user@gmail.com");
        user.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        UserDetails userDetails = new UserDetails();
        userDetails.setLanguage(Language.UKRAINIAN);
        userDetails.setCity(City.KYIV);
        userDetails.setSex("Male");
        user.setUserDetails(userDetails);

        return List.of(admin, user);
    }

    private List<GeneralPage> createDefaultGeneralPage() {

        GeneralPage aboutCinema = new GeneralPage();
        aboutCinema.setTitle("Про кінотеатр");
        aboutCinema.setLanguage(Language.UKRAINIAN);
        aboutCinema.setPageType(PageType.ABOUT_CINEMA);
        aboutCinema.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        aboutCinema.setIsActive(Boolean.TRUE);
        aboutCinema.setSeoBlock(new SeoBlock());

        GeneralPage cafeBar = new GeneralPage();
        cafeBar.setTitle("Кафе-Бар");
        cafeBar.setLanguage(Language.UKRAINIAN);
        cafeBar.setPageType(PageType.CAFE_BAR);
        cafeBar.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        cafeBar.setIsActive(Boolean.TRUE);
        cafeBar.setSeoBlock(new SeoBlock());

        GeneralPage vipHall = new GeneralPage();
        vipHall.setTitle("VIP-зал");
        vipHall.setLanguage(Language.UKRAINIAN);
        vipHall.setPageType(PageType.VIP_HALL);
        vipHall.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        vipHall.setIsActive(Boolean.TRUE);
        vipHall.setSeoBlock(new SeoBlock());

        GeneralPage advertising = new GeneralPage();
        advertising.setTitle("Реклама");
        advertising.setLanguage(Language.UKRAINIAN);
        advertising.setPageType(PageType.ADVERTISING);
        advertising.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        advertising.setIsActive(Boolean.TRUE);
        advertising.setSeoBlock(new SeoBlock());

        GeneralPage childRoom = new GeneralPage();
        childRoom.setTitle("Дитяча кімната");
        childRoom.setLanguage(Language.UKRAINIAN);
        childRoom.setPageType(PageType.CHILD_ROOM);
        childRoom.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        childRoom.setIsActive(Boolean.TRUE);
        childRoom.setSeoBlock(new SeoBlock());

        return List.of(aboutCinema, cafeBar, vipHall, advertising, childRoom);
    }

    private HomePage createDefaultHomePage() {
        HomePage homePage = new HomePage();
        homePage.setTitle("Головна сторінка");
        homePage.setLanguage(Language.UKRAINIAN);
        homePage.setPageType(PageType.HOME_PAGE);
        homePage.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        homePage.setIsActive(Boolean.TRUE);
        homePage.setSeoBlock(new SeoBlock());
        return homePage;
    }

    private ContactPage createDefaultContactPage() {
        ContactPage contactPage = new ContactPage();
        contactPage.setTitle("Контакти");
        contactPage.setLanguage(Language.UKRAINIAN);
        contactPage.setPageType(PageType.CONTACT_PAGE);
        contactPage.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
        ContactCinema contactCinema = new ContactCinema();
        contactCinema.setContactPage(contactPage);
        contactPage.setIsActive(Boolean.TRUE);
        contactPage.setSeoBlock(new SeoBlock());
        return contactPage;
    }

    private Cinema createDefaultCinema() {
        Cinema cinema = new Cinema();
        cinema.setLanguage(Language.UKRAINIAN);
        cinema.setName("Кінотеатр 1");
        cinema.setSeoBlock(new SeoBlock());
        return cinema;
    }

}
