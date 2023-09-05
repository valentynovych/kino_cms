package com.kino_cms.repository;

import com.kino_cms.dto.UserDTO;
import com.kino_cms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT new com.kino_cms.dto.UserDTO(u.id, u.usrname, u.email, u.createTime, " +
            "u.userDetails.firstName,u.userDetails.lastName, u.userDetails.address, " +
            "u.userDetails.cardNumber, u.userDetails.sex, u.userDetails.phone, " +
            "u.userDetails.dateOfBirth, u.userDetails.city, u.userDetails.language) from User u")
    List<UserDTO> getUserDTOList();

    @Query("SELECT new com.kino_cms.dto.UserDTO(u.id, u.usrname, u.email, u.createTime, " +
            "u.userDetails.firstName,u.userDetails.lastName, u.userDetails.address, " +
            "u.userDetails.cardNumber, u.userDetails.sex, u.userDetails.phone, " +
            "u.userDetails.dateOfBirth, u.userDetails.city, u.userDetails.language) from User u where u.id =:id")
    Optional<UserDTO> getUserDTOById(@Param("id") Long id);

    Optional<User> findByEmail(String email);

    @Query("SELECT new com.kino_cms.dto.UserDTO(u.id, u.usrname, u.password, u.email, u.createTime, " +
            "u.userDetails.firstName,u.userDetails.lastName, u.userDetails.address, " +
            "u.userDetails.cardNumber, u.userDetails.sex, u.userDetails.phone, " +
            "u.userDetails.dateOfBirth, u.userDetails.city, u.userDetails.language) from User u where u.email =:email")
    Optional<UserDTO> getUserDTOByEmail(@Param("email") String email);
}
