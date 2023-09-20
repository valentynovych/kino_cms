package com.kino_cms.service;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.entity.FeedPage;
import com.kino_cms.entity.User;
import com.kino_cms.entity.UserDetails;
import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.Role;
import com.kino_cms.repository.FeedPageRepo;
import com.kino_cms.repository.FilmRepo;
import com.kino_cms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    FilmRepo filmRepo;
    @Mock
    FeedPageRepo feedPageRepo;
    StatisticService statisticService;

    @BeforeEach
    void setUp() {
        statisticService = new StatisticService(userRepository, filmRepo, feedPageRepo);
    }
    @Test
    void getUserCount() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User(), new User()));
        Integer userCount = statisticService.getUserCount();
        assertEquals(3, userCount);
    }

    @Test
    void getActivePromotions() {
        FeedPage feedPage = new FeedPage();
        feedPage.setIsActivate(Boolean.TRUE);
        feedPage.setFeedType(FeedType.PROMOTION);
        FeedPage feedPage1 = new FeedPage();
        feedPage1.setIsActivate(Boolean.TRUE);
        feedPage1.setFeedType(FeedType.PROMOTION);
        FeedPage feedPage2 = new FeedPage();
        feedPage2.setIsActivate(Boolean.FALSE);
        feedPage2.setFeedType(FeedType.PROMOTION);
        when(feedPageRepo.findFeedPagesByFeedType(FeedType.PROMOTION)).thenReturn(List.of(
                feedPage, feedPage1, feedPage2));
        List<FeedPage> activePromotions = statisticService.getActivePromotions();
        assertFalse(activePromotions.isEmpty());
        assertEquals(2, activePromotions.size());
        assertTrue(activePromotions.get(0).getIsActivate());
        assertEquals(FeedType.PROMOTION, activePromotions.get(0).getFeedType());
    }

    @Test
    void getCountFilmIsReleasedNow() {
        when(filmRepo.getAllFilmIsReleasedNow(Language.UKRAINIAN)).thenReturn(List.of(
                new FilmDTO(), new FilmDTO(), new FilmDTO(), new FilmDTO()));
        Integer userCount = statisticService.getCountFilmIsReleasedNow();
        assertEquals(4, userCount);
    }

    @Test
    void getUsersGender() {
        User user = new User();
        user.setRole(Role.ROLE_USER);
        UserDetails userDetails = new UserDetails();
        userDetails.setSex("Male");
        user.setUserDetails(userDetails);
        User user1 = new User();
        user1.setRole(Role.ROLE_USER);
        UserDetails userDetails1 = new UserDetails();
        userDetails1.setSex("Male");
        user1.setUserDetails(userDetails1);
        User user2 = new User();
        user2.setRole(Role.ROLE_USER);
        UserDetails userDetails2 = new UserDetails();
        userDetails2.setSex("Female");
        user2.setUserDetails(userDetails2);
        User user3 = new User();
        user3.setRole(Role.ROLE_USER);
        UserDetails userDetails3 = new UserDetails();
        userDetails3.setSex("Female");
        user3.setUserDetails(userDetails3);

        when(userRepository.findAll()).thenReturn(List.of(user, user1, user2, user3));
        Map<String, Integer> usersGender = statisticService.getUsersGender();
        assertNotNull(usersGender);
        assertEquals(2, usersGender.size());
        Integer male = usersGender.get("Male");
        Integer female = usersGender.get("Female");

        assertEquals(2, male);
        assertEquals(2, female);
    }
}