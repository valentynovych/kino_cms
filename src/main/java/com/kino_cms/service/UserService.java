package com.kino_cms.service;

import com.kino_cms.dto.RegistrationUserDto;
import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.User;
import com.kino_cms.entity.UserDetails;
import com.kino_cms.enums.City;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.Role;
import com.kino_cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getUserDTOList() {
        log.info("-> execute method getUserDTOList without parameters");
        return userRepository.getUserDTOList();
    }

    public Optional<UserDTO> getUserDTOById(Long id) {
        log.info("-> execute method getUserDTOById: " + id);
        return userRepository.getUserDTOById(id);
    }

    public Optional<User> findById(Long id) {
        log.info("-> execute method findById: " + id);
        return userRepository.findById(id);
    }

    public void delete(User user) {
        log.info("-> execute method delete by user: " + user.getId());
        userRepository.delete(user);
    }

    public void saveUserDTO(UserDTO userDTO) {
        log.info("-> start execute method saveUserDTO: " + userDTO.getEmail());

        Optional<User> userOnDB = userRepository.findById(userDTO.getId());
        User user = userOnDB.orElseGet(this::createNewUser);
        UserDetails userDetails;
        if (user.getUserDetails() == null) {
            userDetails = new UserDetails();
        } else {
            userDetails = user.getUserDetails();
        }

        user.setEmail(userDTO.getEmail());
        user.setUsrname(userDTO.getUsername());
        user.setCreateTime(userDTO.getCreateTime());

        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userDetails.setSex(userDTO.getSex());
        userDetails.setCity(userDTO.getCity());
        userDetails.setLanguage(userDTO.getLanguage());
        userDetails.setAddress(userDTO.getAddress());
        userDetails.setPhone(userDTO.getPhone());
        userDetails.setCardNumber(userDTO.getCardNumber());
        userDetails.setFirstName(userDTO.getFirstName());
        userDetails.setLastName(userDTO.getLastName());
        if (userDTO.getDateOfBirth() != null) {
            userDetails.setDateOfBirth(new Date(userDTO.getDateOfBirth().getTime()));
        }
        user.setUserDetails(userDetails);
        log.info("-> exit from method saveUserDTO: " + userDTO.getEmail());
        saveUser(user);
    }

    public Optional<User> findByEmail(String userEmail) {
        log.info("-> start execute method findByEmail: " + userEmail);
        return userRepository.findByEmail(userEmail);
    }

    public void saveUser(User user) {
        log.info("-> start execute method saveUser with email: " + user.getEmail());
        userRepository.save(user);
    }

    private User createNewUser() {
        log.info("-> start execute method createNewUser ");
        User user = new User();
        user.setUserDetails(new UserDetails());
        user.setRole(Role.ROLE_USER);
        user.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        log.info("-> exit from method createNewUser: ");
        return user;
    }


    public void registerUser(RegistrationUserDto registrationUserDto) {
        log.info("-> start execute method registerUser, with email: " + registrationUserDto.getEmail());
        UserDTO user = new UserDTO();
        user.setId(0L);
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(registrationUserDto.getPassword());
        user.setFirstName(registrationUserDto.getFirstName());
        user.setLastName(registrationUserDto.getLastName());
        user.setPhone(registrationUserDto.getPhoneNumber());
        user.setCity(City.KYIV);
        user.setLanguage(Language.UKRAINIAN);
        user.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        log.info("-> exit from method createNewUser ");
        saveUserDTO(user);
    }

    public UserDTO getUserDTOByEmail(String email) {
        log.info("-> start execute method getUserDTOByEmail by email: " + email);
        Optional<UserDTO> byEmail = userRepository.getUserDTOByEmail(email);
        log.info("-> exit from method getUserDTOByEmail by email: " + email);
        return byEmail.orElseGet(UserDTO::new);
    }

    public boolean passwordIsCorrect(String password, String username) {
        log.info("-> start execute method getUserDTOByEmail by email: " + username);
        Optional<User> userOptional = findByEmail(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
