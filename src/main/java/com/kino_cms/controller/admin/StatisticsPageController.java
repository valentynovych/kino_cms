package com.kino_cms.controller.admin;

import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.FeedPage;
import com.kino_cms.entity.UserSession;
import com.kino_cms.enums.FeedType;
import com.kino_cms.service.FeedPageService;
import com.kino_cms.service.StatisticService;
import com.kino_cms.service.UserService;
import com.kino_cms.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StatisticsPageController {

    private final StatisticService statisticService;
    private final UserSessionService userSessionService;
    @Autowired
    FeedPageService feedPageService;
    @GetMapping("/admin/stat")
    public String viewStatistics(Model model){

        Integer userCount = statisticService.getUserCount();
        model.addAttribute("userCount", userCount);

        List<FeedPage> feedPages = statisticService.getActivePromotions();
        model.addAttribute("feedPages", feedPages);

        Integer filmCount = statisticService.getCountFilmIsReleasedNow();
        model.addAttribute("filmCount", filmCount);

        Map<String, Integer> usersGender = statisticService.getUsersGender();
        model.addAttribute("usersGender", usersGender);

        return "admin/stat/statPage";
    }

    @GetMapping("/admin/getSessionsLastWeek")
    public @ResponseBody Map<String, Integer> getSessionsLastWeek() {
        return userSessionService.getLastWeekActivity();
    }
}
