package com.kino_cms.service;

import com.kino_cms.entity.User;
import com.kino_cms.entity.UserSession;
import com.kino_cms.repository.UserSessionRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserSessionService {

    private final UserService userService;
    private final UserSessionRepo userSessionRepo;

    public void createSession(HttpSession httpSession) {
        log.info("-> start execution method createSession() with HttpSessionId:" + httpSession.getId());
        UserSession userSession = new UserSession();
        userSession.setOpenSession(LocalDateTime.now());
        userSession.setHttpSessionId(httpSession.getId());

        SecurityContext context = SecurityContextHolder.getContext();
        log.info("-> getContext from SecurityContextHolder");
        String name = context.getAuthentication().getName();
        if (name != null) {
            log.info("-> context have Authentication user with name: " + name);
            Optional<User> byEmail = userService.findByEmail(name);
            if (byEmail.isPresent()) {
                log.info("-> user from context ");
                userSession.setUser(byEmail.get());
            }
        }
        saveSession(userSession);
        log.info("-> exit from method createSession()");
    }

    public void saveSession(UserSession userSession) {
        log.info("-> start execution method saveSession() with sessionId:" + userSession.getHttpSessionId());
        Optional<UserSession> byHttpSessionId = userSessionRepo.findByHttpSessionId(userSession.getHttpSessionId());
        if (byHttpSessionId.isEmpty()) {
            log.info("-> session already isPresent, update session on DB");
            userSessionRepo.save(userSession);
        }
        if (byHttpSessionId.isPresent() && userSession.getUser() != null) {
            log.info("-> session already isPresent end userSession User isPresent, update session on DB");
            UserSession userSession1 = byHttpSessionId.get();
            userSession1.setUser(userSession.getUser());
            userSessionRepo.save(userSession1);
        }
        log.info("-> exit from method saveSession()");
    }

    public Map<String, Integer> getLastWeekActivity() {
        log.info("-> start execution method getLastWeekActivity()");
        LocalDateTime today = LocalDate.now().atStartOfDay();
        Map<String, Integer> sessionOnDay = new LinkedHashMap<>();
        log.info("-> getLastWeekActivity > create user activity on the last weeks before: " + today);
        for (int i = 6; i >= 0; i--) {
            LocalDateTime dateTime = today.minusDays(i);
            List<UserSession> userSessionsByOpenSession =
                    userSessionRepo.getUserSessionsByOpenSessionAfterAndOpenSessionBefore(dateTime, dateTime.plusDays(1));
            String dayName = StringUtils.capitalize(dateTime
                    .format(DateTimeFormatter.ofPattern("E dd.MM", new Locale("uk"))));
            sessionOnDay.put(dayName, userSessionsByOpenSession.size());
        }
        log.info("-> exit from method getLastWeekActivity(), return Map size: " + sessionOnDay.size());
        return sessionOnDay;
    }
}
