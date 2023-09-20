package com.kino_cms.service;

import com.kino_cms.entity.User;
import com.kino_cms.entity.UserSession;
import com.kino_cms.repository.UserSessionRepo;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSessionServiceTest {

    @Mock
    UserService userService;
    @Mock
    UserSessionRepo userSessionRepo;
    @Mock
    HttpSession httpSession;
    UserSessionService userSessionService;
    private User user;

    @BeforeEach
    void setUp() {
        userSessionService = new UserSessionService(userService, userSessionRepo);
        user = new User();
        user.setId(1L);
        user.setEmail("mail@mail.com");
        user.setPassword("password");
    }

    @Test
    void createSession() {

        httpSession = new MockHttpSession();
        SecurityContext context = new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>()));
        SecurityContextHolder.setContext(context);

        userSessionService.createSession(httpSession);
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        userSessionService.createSession(httpSession);
    }

    @Test
    void saveSession() {

        String sessionId = "session-id";
        UserSession userSession = new UserSession();
        userSession.setHttpSessionId(sessionId);
        userSession.setUser(user);
        userSession.setOpenSession(LocalDateTime.now());
        when(userSessionRepo.findByHttpSessionId(sessionId)).thenReturn(Optional.of(userSession));

        // When
        userSessionService.saveSession(userSession);

        verify(userSessionRepo).save(userSession);
    }

    @Test
    void getLastWeekActivity() {
        LocalDateTime today = LocalDateTime.now();
        List<UserSession> userSessions = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDateTime dateTime = today.minusDays(i);
            UserSession userSession = new UserSession();
            userSession.setOpenSession(dateTime);
            userSessions.add(userSession);
        }
        when(userSessionRepo.getUserSessionsByOpenSessionAfterAndOpenSessionBefore(
                any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(userSessions);

        Map<String, Integer> lastWeekActivity = userSessionService.getLastWeekActivity();
        assertFalse(lastWeekActivity.isEmpty());
        assertEquals(7, lastWeekActivity.size());
    }
}