package com.kino_cms.dto;

import com.kino_cms.enums.City;
import com.kino_cms.enums.Language;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.sql.Date;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String createTime;
    private String firstName;
    private String lastName;
    private String address;
    private String cardNumber;
    private String sex;
    private String phone;
    private Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    private City city;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Boolean forMailing;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String createTime,
                   String firstName, String lastName, String address,
                   String cardNumber, String sex, String phone, Date dateOfBirth,
                   City city, Language language) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createTime = createTime;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.cardNumber = cardNumber;
        this.sex = sex;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.language = language;
    }
}
