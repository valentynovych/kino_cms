package com.kino_cms.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MailingDTO {
    private List<UserDTO> userDTOS;
    private List<String> lastTemplates;
    private String lastUsedTemplate;
    private Integer countMails;
    private MultipartFile htmlTemplate;

    public MailingDTO() {
    }

    public MailingDTO(List<UserDTO> userDTOS, List<String> lastTemplates, String lastUsedTemplate, Integer countMails, MultipartFile htmlTemplate) {
        this.userDTOS = userDTOS;
        this.lastTemplates = lastTemplates;
        this.lastUsedTemplate = lastUsedTemplate;
        this.countMails = countMails;
        this.htmlTemplate = htmlTemplate;
    }

    public List<UserDTO> getUserDTOS() {
        return userDTOS;
    }

    public void setUserDTOS(List<UserDTO> userDTOS) {
        this.userDTOS = userDTOS;
    }

    public List<String> getLastTemplates() {
        return lastTemplates;
    }

    public void setLastTemplates(List<String> lastTemplates) {
        this.lastTemplates = lastTemplates;
    }

    public Integer getCountMails() {
        return countMails;
    }

    public void setCountMails(Integer countMails) {
        this.countMails = countMails;
    }

    public MultipartFile getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(MultipartFile htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }

    public String getLastUsedTemplate() {
        return lastUsedTemplate;
    }

    public void setLastUsedTemplate(String lastUsedTemplate) {
        this.lastUsedTemplate = lastUsedTemplate;
    }
}
