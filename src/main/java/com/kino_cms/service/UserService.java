package com.kino_cms.service;

import com.kino_cms.dto.RegistrationUserDto;
import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.User;
import com.kino_cms.entity.UserDetails;
import com.kino_cms.enums.Role;
import com.kino_cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getUserDTOList() {

        return userRepository.getUserDTOList();
    }

    public Optional<UserDTO> getUserDTOById(Long id) {
        return userRepository.getUserDTOById(id);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void saveUserDTO(UserDTO userDTO) {
        Optional<User> userOnDB = userRepository.findById(userDTO.getId());
        User user;
        user = userOnDB.orElseGet(this::createNewUser);
        UserDetails userDetails = user.getUserDetails();

        user.setEmail(userDTO.getEmail());
        user.setUsrname(userDTO.getUsername());
        user.setCreateTime(userDTO.getCreateTime());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userDetails.setSex(userDTO.getSex());
        userDetails.setCity(userDTO.getCity());
        userDetails.setLanguage(userDTO.getLanguage());
        userDetails.setAddress(userDTO.getAddress());
        userDetails.setPhone(userDTO.getPhone());
        userDetails.setCardNumber(userDTO.getCardNumber());
        userDetails.setFirstName(userDTO.getFirstName());
        userDetails.setLastName(userDTO.getLastName());
        userDetails.setDateOfBirth(userDTO.getDateOfBirth());
        user.setUserDetails(userDetails);

        userRepository.save(user);
    }

    public Optional<User> findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    private User createNewUser() {
        User user = new User();
        user.setUserDetails(new UserDetails());
        user.setRole(Role.ROLE_USER);
        user.setCreateTime(new Date().toString());
        return user;
    }


    public void registerUser(RegistrationUserDto registrationUserDto) {
        UserDTO user = new UserDTO();
        user.setId(0L);
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(registrationUserDto.getPassword());
        user.setFirstName(registrationUserDto.getFirstName());
        user.setLastName(registrationUserDto.getLastName());
        user.setPhone(registrationUserDto.getPhoneNumber());
        saveUserDTO(user);
    }

    public UserDTO getUserDTOByEmail(String email) {
        Optional<UserDTO> byEmail = userRepository.getUserDTOByEmail(email);
        return byEmail.orElseGet(UserDTO::new);
    }

    public boolean passwordIsCorrect(String password, String username) {
        Optional<User> userOptional = findByEmail(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
