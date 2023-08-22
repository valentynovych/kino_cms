package com.kino_cms.controller;

import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.FeedPage;
import com.kino_cms.enums.FeedType;
import com.kino_cms.service.FeedPageService;
import com.kino_cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StatisticsPageController {
    @Autowired
    UserService userService;
    @Autowired
    FeedPageService feedPageService;
    @GetMapping("/admin/stat")
    public String viewStatistics(Model model){
        List<UserDTO> userDTOS = userService.getUserDTOList();
        Integer userCount = userDTOS.size();

        model.addAttribute("userCount", userCount);

        List<FeedPage> feedPages = feedPageService.findFeedPagesByFeedType(FeedType.PROMOTION);
        model.addAttribute("feedPages", feedPages
                .stream()
                .filter(FeedPage::getIsActivate)
                .toList());
        return "admin/stat/statPage";
    }
}
