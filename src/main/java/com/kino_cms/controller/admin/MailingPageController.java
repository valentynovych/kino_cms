package com.kino_cms.controller.admin;

import com.kino_cms.dto.MailingDTO;
import com.kino_cms.dto.UserDTO;
import com.kino_cms.service.MailSenderService;
import com.kino_cms.service.MailingService;
import com.kino_cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MailingPageController {

    @Autowired
    MailSenderService mailSenderService;
    @Autowired
    MailingService mailingService;
    @Autowired
    UserService userService;
    @Value("${upload.path}")
    private String uploadPath;
    private static Double progress = 0.0;

    @GetMapping("/admin/mailing")
    public String viewMailingPage(Model model,
                                  @ModelAttribute("mailingDTO") MailingDTO mailingDTO) {
        List<UserDTO> userForMailing = mailingDTO.getUserDTOS();

        if (userForMailing == null || userForMailing.isEmpty()) {
            userForMailing = userService.getUserDTOList();
        } else {
            userForMailing = userForMailing.stream().filter(UserDTO::getForMailing).toList();
        }

        MailingDTO entity = mailingService.getMailingEntity();
        mailingDTO.setCountMails(entity.getCountMails());
        mailingDTO.setLastTemplates(entity.getLastTemplates());
        mailingDTO.setLastUsedTemplate(entity.getLastUsedTemplate());
        mailingDTO.setUserDTOS(userForMailing);
        model.addAttribute("mailingDTO", mailingDTO);

        return "admin/mailing/mailingPage";
    }

    @ResponseBody
    @PostMapping("/admin/mailing")
    public void sendMails(@ModelAttribute MailingDTO mailingDTO) throws IOException {
        progress = 0.0;
        List<UserDTO> users = mailingDTO.getUserDTOS();
        int countUsersToSend = users.size();
        MultipartFile template = mailingDTO.getHtmlTemplate();
        Integer allCountMails = mailingDTO.getCountMails();
        List<String> templates = mailingDTO.getLastTemplates();
        if (templates == null) {
            templates = new ArrayList<>();
        }

        if (!template.isEmpty()){
            File directory = new File(uploadPath + "/html_templates/");
            if (!directory.exists()) directory.mkdir();
            template.transferTo(new File(uploadPath + "/html_templates/" + template.getOriginalFilename()));
            templates.add(0, template.getOriginalFilename());
            mailingDTO.setLastTemplates(templates.stream().limit(5).toList());
            mailingDTO.setLastUsedTemplate(template.getOriginalFilename());
        }
        for (int i = 0; i < countUsersToSend; i++) {
            String mailTo = users.get(i).getEmail();
            mailSenderService.sendEmail(mailTo, mailingDTO.getLastUsedTemplate());
            //Thread.sleep(1000);
            progress = (i + 1) / (double) countUsersToSend * 100;
            allCountMails++;
        }

        mailingDTO.setCountMails(allCountMails);
        mailingService.saveMailingEntity(mailingDTO);
    }

    @GetMapping("/admin/chose-users")
    public String choseUsers(Model model) {
        MailingDTO mailingDTO = new MailingDTO();
        mailingDTO.setUserDTOS(userService.getUserDTOList());
        model.addAttribute("mailingDTO", mailingDTO);
        return "admin/mailing/choseUsers";
    }

    @PostMapping("/admin/chose-users")
    public String users(@ModelAttribute MailingDTO mailingDTO, RedirectAttributes model) {
        model.addFlashAttribute("mailingDTO", mailingDTO);
        return "redirect:/admin/mailing";
    }
    @GetMapping("/admin/progress")
    public ResponseEntity<Double> getProgress() throws InterruptedException {
        Thread.sleep(300);
        return ResponseEntity.ok(progress);
    }
}
