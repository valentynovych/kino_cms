package com.kino_cms.controller;

import com.kino_cms.dto.LoginRequest;
import com.kino_cms.dto.RegistrationUserDto;
import com.kino_cms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("login");
        LoginRequest loginRequest = new LoginRequest();
        modelAndView.addObject("form", loginRequest);
        if (Strings.isNotEmpty(error)) modelAndView.addObject("loginError", error);
        return modelAndView;
    }

    @PostMapping("/login")
    public void authenticateUser(@ModelAttribute("form") LoginRequest loginRequest,
                                 HttpServletRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                    loginRequest.getPassword()));
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
        } catch (BadCredentialsException e) {
            new ModelAndView("redirect:user_views/homePageView", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/signup")
    public ModelAndView viewSignupPage() {
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("userDto", new RegistrationUserDto());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView registerUser(@ModelAttribute("userDto") RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ModelAndView("signup").addObject("messageError", "Паролі не співпадають");
        }
        if (userService.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            return new ModelAndView("signup").addObject("messageError",
                    String.format("Користувач з адресою %s уже зареєстрований", registrationUserDto.getEmail()));
        }
        userService.registerUser(registrationUserDto);

        return new ModelAndView("redirect:/login");

    }

    @GetMapping("/logout")
    public ModelAndView logoutUser() {
        return new ModelAndView("redirect:/");
    }

}
