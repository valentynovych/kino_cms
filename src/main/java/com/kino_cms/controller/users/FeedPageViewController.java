package com.kino_cms.controller.users;

import com.kino_cms.dto.FeedDTO;
import com.kino_cms.service.FeedPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class FeedPageViewController {

    @Autowired
    FeedPageService feedPageService;

    @GetMapping("/feeds")
    public ModelAndView viewAllFeeds() {
        ModelAndView modelAndView = new ModelAndView("user_views/feed_promotion/allFeedsViewPage");
        List<FeedDTO> allFeedPages = feedPageService.getAllFeedDTOByFeedTypeFeed();
        modelAndView.addObject("allFeedPages", allFeedPages);
        return modelAndView;
    }

    @GetMapping("/promotions")
    public ModelAndView viewAllPromotions() {
        ModelAndView modelAndView = new ModelAndView("user_views/feed_promotion/allFeedsViewPage");
        List<FeedDTO> allFeedPages = feedPageService.getAllFeedDTOByFeedTypePromotion();
        modelAndView.addObject("allFeedPages", allFeedPages);
        return modelAndView;
    }

    @GetMapping("/feed/{feedId}")
    public ModelAndView viewFeed(@PathVariable Long feedId) {
        ModelAndView modelAndView = new ModelAndView("user_views/feed_promotion/feedViewPage");
        FeedDTO feedDTO = feedPageService.getFeedDTOById(feedId);
        modelAndView.addObject("feedPage", feedDTO);
        return modelAndView;
    }

}
