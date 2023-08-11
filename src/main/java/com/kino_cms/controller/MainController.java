package com.kino_cms.controller;

import com.kino_cms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/1")
    public String main(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "main";
    }
}
