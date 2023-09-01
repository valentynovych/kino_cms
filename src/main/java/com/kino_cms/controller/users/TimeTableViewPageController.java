package com.kino_cms.controller.users;

import com.kino_cms.entity.FilmSession;
import com.kino_cms.service.FilmSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TimeTableViewPageController {

    @Autowired
    FilmSessionService filmSessionService;

    @GetMapping("/timetable")
    public ModelAndView viewTimetable() {
        ModelAndView modelAndView = new ModelAndView("user_views/session_timetable/timetableViewPage");
        List<FilmSession> filmSessionList = filmSessionService.getAllSession();
        modelAndView.addObject("listSessions", filmSessionList);
        return modelAndView;
    }

    @GetMapping("/session/{sessionId}")
    public ModelAndView viewSession(@PathVariable Long sessionId) {
        ModelAndView modelAndView = new ModelAndView("user_views/session_timetable/sessionViewPage");
        FilmSession filmSession = filmSessionService.getSessionById(sessionId);
        modelAndView.addObject("filmSession", filmSession);
        return modelAndView;
    }
}
