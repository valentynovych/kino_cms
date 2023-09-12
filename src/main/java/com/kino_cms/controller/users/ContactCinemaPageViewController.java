package com.kino_cms.controller.users;

import com.kino_cms.entity.ContactPage;
import com.kino_cms.service.ContactPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactCinemaPageViewController {

    @Autowired
    ContactPageService contactPageService;

    @GetMapping("/contacts")
    public ModelAndView viewContacts() {
        ModelAndView modelAndView = new ModelAndView("user_views/contacts/contactsViewPage");
        ContactPage contactPage = contactPageService.getContactPage().get();
        modelAndView.addObject("contactPage", contactPage);
        return modelAndView;
    }
}
