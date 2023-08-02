package com.kino_cms.controller;

import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.User;
import com.kino_cms.entity.UserDetails;
import com.kino_cms.enums.City;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.UserRepository;
import com.kino_cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/admin/view-users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.getUserDTOList());
        return "/admin/view_users";
    }

//    @GetMapping("/admin/edit-user")
//    public String addUser(Model model) {
//        User user = new User();
//        UserDetails userDetails = new UserDetails();
//        userDetails.setLanguage(Language.UKRAINIAN);
//        userDetails.setSex("Male");
//        user.setUserDetails(userDetails);
//        model.addAttribute("userForm", user);
//        return "/admin/edit_user";
//    }

    @PostMapping("/admin/edit-user/{id}")
    public String saveUser(@ModelAttribute UserDTO user, @PathVariable Long id) {

        if (user.getCreateTime() == null){
            user.setCreateTime(new Timestamp(new Date().getTime()));
        }
        System.out.println(user);

        userService.saveUserDTO(user);

        return "redirect:/admin/view-users";
    }

    @GetMapping("admin/edit-user/{id}")
    public String editUser(Model model, @PathVariable Long id) {
        Optional<UserDTO> optionalUser = userService.getUserDTOById(id);
        UserDTO user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        model.addAttribute("user", user);
        return "/admin/edit_user";
    }

    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Long id){
        Optional<User> optionalUser = userService.findById(id);
        optionalUser.ifPresent(user -> userService.delete(user));
        return "redirect:/admin/view-users";
    }
    @ModelAttribute("allCities")
    public List<String> allCity() {
        List<String> cities = Arrays.stream(City.values())
                .map(city -> city.name())
                .collect(Collectors.toList());
        return cities;
    }
}
