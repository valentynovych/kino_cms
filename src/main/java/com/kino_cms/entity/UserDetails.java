package com.kino_cms.entity;

import com.kino_cms.enums.City;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
@NoArgsConstructor
@Data
@Entity
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String firstName;
    String lastName;
    String address;
    String cardNumber;
    String sex;
    String phone;
    Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    City city;

}
