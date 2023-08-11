package com.kino_cms.service;

import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.User;
import com.kino_cms.entity.UserDetails;
import com.kino_cms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
        UserDetails userDetails;
        if (userOnDB.isPresent()) {
            user = userOnDB.get();
            userDetails = user.getUserDetails();
        } else {
            user = new User();
            userDetails = new UserDetails();
        }

        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setCreateTime(userDTO.getCreateTime());

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
}
