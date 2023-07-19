package com.kino_cms.controller;

import com.kino_cms.entity.User;
import com.kino_cms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user-page")
    public String addUser(Model model){
        model.addAttribute("userForm", new User());
        return "user_page";
    }
//    @GetMapping
//    public String printUsers(Map<String, Object> model){
//        Iterable<User> users = userRepository.findAll();
//        model.put("users", users);
//        return "user_page";
//    }
}
