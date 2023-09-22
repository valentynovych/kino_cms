package com.kino_cms.service;

import com.kino_cms.dto.RegistrationUserDto;
import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.User;
import com.kino_cms.entity.UserDetails;
import com.kino_cms.enums.City;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.Role;
import com.kino_cms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void getUserDTOList() {
        UserDTO userDTO1 = new UserDTO(1L, "username1", "mail@mail.com", "23-11-2020", "First name", "Last Name",
                "Address", "8888 8888 8888 8888", "MALE", "3800088008", Date.valueOf(LocalDate.now()),
                City.KYIV, Language.UKRAINIAN);
        UserDTO userDTO2 = new UserDTO(2L, "username2", "mail1@mail.com", "23-11-2020", "First name", "Last Name",
                "Address", "8888 8888 8888 8888", "MALE", "3800088008", Date.valueOf(LocalDate.now()),
                City.KYIV, Language.UKRAINIAN);

        when(userRepository.getUserDTOList()).thenReturn(List.of(userDTO1, userDTO2));
        // When
        List<UserDTO> userDTOS = userService.getUserDTOList();

        // Then
        assertEquals(2, userDTOS.size());
        assertEquals(userDTO1.getEmail(), userDTOS.get(0).getEmail());
        assertEquals(userDTO2.getEmail(), userDTOS.get(1).getEmail());
    }

    @Test
    void getUserDTOById() {
        UserDTO userDTO1 = new UserDTO(1L, "username1", "mail@mail.com", "23-11-2020", "First name", "Last Name",
                "Address", "8888 8888 8888 8888", "MALE", "3800088008", Date.valueOf(LocalDate.now()),
                City.KYIV, Language.UKRAINIAN);
        when(userRepository.getUserDTOById(1L)).thenReturn(Optional.of(userDTO1));

        Optional<UserDTO> userDTO = userService.getUserDTOById(1L);
        assertTrue(userDTO.isPresent());
        assertEquals(userDTO1.getEmail(), userDTO.get().getEmail());
        assertEquals(userDTO1.getCreateTime(), userDTO.get().getCreateTime());
    }

    @Test
    void findById() {
        User user = new User();
        user.setId(1L);
        user.setEmail("admin@admin.com");
        user.setPassword("simplePassword");
        user.setRole(Role.ROLE_ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> userOptional = userService.findById(1L);

        assertTrue(userOptional.isPresent());
        assertEquals(user.getEmail(), userOptional.get().getEmail());
        assertEquals(user.getRole(), userOptional.get().getRole());
    }

    @Test
    void delete() {
        User user = new User();
        user.setId(1L);
        user.setEmail("admin@admin.com");
        user.setPassword("simplePassword");
        user.setRole(Role.ROLE_ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.delete(userService.findById(1L).get());

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(userService.findById(1L), Optional.empty());

    }

    @Test
    void saveUserDTO() {
        UserDTO userDTO1 = new UserDTO(1L, "username1", "testmail@mail.com", "23-11-2020", "First name", "Last Name",
                "Address", "8888 8888 8888 8888", "MALE", "3800088008", Date.valueOf(LocalDate.now()),
                City.KYIV, Language.UKRAINIAN);
        userDTO1.setPassword("password");

        userService.saveUserDTO(userDTO1);


        User user = new User();
        UserDetails userDetails = new UserDetails();
        user.setEmail(userDTO1.getEmail());
        user.setUsrname(userDTO1.getUsername());
        user.setCreateTime(userDTO1.getCreateTime());
        user.setPassword(passwordEncoder.encode(userDTO1.getPassword()));

        userDetails.setSex(userDTO1.getSex());
        userDetails.setCity(userDTO1.getCity());
        userDetails.setLanguage(userDTO1.getLanguage());
        userDetails.setAddress(userDTO1.getAddress());
        userDetails.setPhone(userDTO1.getPhone());
        userDetails.setCardNumber(userDTO1.getCardNumber());
        userDetails.setFirstName(userDTO1.getFirstName());
        userDetails.setLastName(userDTO1.getLastName());
        userDetails.setDateOfBirth(userDTO1.getDateOfBirth());
        user.setUserDetails(userDetails);

        when(userRepository.findByEmail(userDTO1.getEmail())).thenReturn(Optional.of(user));

        Optional<User> userOptional = userService.findByEmail(userDTO1.getEmail());
        assertTrue(userOptional.isPresent());
        User user1 = userOptional.get();
        assertEquals(userDTO1.getEmail(), user1.getEmail());
        assertTrue(passwordEncoder.matches(userDTO1.getPassword(), user1.getPassword()));
        assertEquals(userDTO1.getCreateTime(), user1.getCreateTime());
        assertEquals(userDTO1.getLanguage(), user1.getUserDetails().getLanguage());
        assertEquals(userDTO1.getDateOfBirth(), user1.getUserDetails().getDateOfBirth());

        user.setUserDetails(null);
        when(userRepository.findById(userDTO1.getId())).thenReturn(Optional.of(user));
        userService.saveUserDTO(userDTO1);
    }

    @Test
    void findByEmail() {
        User user = new User();
        user.setId(1L);
        user.setEmail("admin@admin.com");
        user.setPassword("simplePassword");
        user.setRole(Role.ROLE_ADMIN);

        when(userRepository.findByEmail("admin@admin.com")).thenReturn(Optional.of(user));

        Optional<User> userOptional = userService.findByEmail("admin@admin.com");

        assertTrue(userOptional.isPresent());
        assertEquals(user.getEmail(), userOptional.get().getEmail());
        assertEquals(user.getRole(), userOptional.get().getRole());
    }

    @Test
    void saveUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("admin@admin.com");
        user.setPassword("simplePassword");
        user.setRole(Role.ROLE_ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.saveUser(user);

        Optional<User> userOptional = userService.findById(1L);
        assertTrue(userOptional.isPresent());
        assertEquals(user.getEmail(), userOptional.get().getEmail());
    }

    @Test
    void registerUser() {
        RegistrationUserDto userDto = new RegistrationUserDto("FirstName", "LastName", "3800088008",
                "username", "password", "password", "testmail@mail.com", Role.ROLE_USER);
        userService.registerUser(userDto);
        User user = new User();
        user.setId(1L);
        user.setEmail("testmail@mail.com");
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);
        user.setCreateTime(Date.valueOf(LocalDate.now()).toString());

        when(userRepository.findByEmail("testmail@mail.com")).thenReturn(Optional.of(user));

        Optional<User> byEmail = userService.findByEmail(userDto.getEmail());
        assertTrue(byEmail.isPresent());

    }

    @Test
    void getUserDTOByEmail() {
        UserDTO userDTO1 = new UserDTO(1L, "username1", "mail@mail.com", "23-11-2020", "First name", "Last Name",
                "Address", "8888 8888 8888 8888", "MALE", "3800088008", Date.valueOf(LocalDate.now()),
                City.KYIV, Language.UKRAINIAN);
        when(userRepository.getUserDTOByEmail("mail@mail.com")).thenReturn(Optional.of(userDTO1));

        UserDTO userDTO = userService.getUserDTOByEmail("mail@mail.com");
        assertNotNull(userDTO);
        assertEquals(userDTO1.getEmail(), userDTO.getEmail());
        assertEquals(userDTO1.getCreateTime(), userDTO.getCreateTime());
    }

    @Test
    void passwordIsCorrect() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testmail@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.ROLE_USER);
        when(userRepository.findByEmail("testmail@mail.com")).thenReturn(Optional.of(user));

        boolean isCorrect = userService.passwordIsCorrect("password", "testmail@mail.com");
        assertTrue(isCorrect);

        boolean isInCorrect = userService.passwordIsCorrect("password", "testmail1@mail.com");
        assertFalse(isInCorrect);
    }
}