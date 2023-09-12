package com.kino_cms.controller.users;

import com.kino_cms.dto.UserDTO;
import com.kino_cms.enums.City;
import com.kino_cms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserPageController {
    private final UserService userService;

    @GetMapping
    public ModelAndView viewProfile(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("profile");
        String principalName = principal.getName();
        modelAndView.addObject("userDto", userService.getUserDTOByEmail(principalName));
        return modelAndView;
    }

    @PostMapping
    public ModelAndView saveUserProfile(@ModelAttribute("userDto") UserDTO userDTO,
                                        @ModelAttribute("confirmPassword") String confirmPassword) {
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("userDto", userDTO);
        String password = userDTO.getPassword();
        if (!password.equals(confirmPassword)) {
            modelAndView.addObject("errorMessage", "Пароль та підтвердження не співпадають");
            return modelAndView;
        }
        if (!userService.passwordIsCorrect(password, userDTO.getEmail())) {
            modelAndView.addObject("errorMessage", "Не вірний пароль");
            return modelAndView;
        }

        userService.saveUserDTO(userDTO);
        return modelAndView;
    }

    @ModelAttribute("allCities")
    public List<String> allCity() {
        return Arrays.stream(City.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
