package com.kino_cms.dto;

import com.kino_cms.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUserDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private Role role;
}
