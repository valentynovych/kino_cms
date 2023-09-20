package com.kino_cms.service;

import com.kino_cms.entity.User;
import com.kino_cms.enums.Role;
import com.kino_cms.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    UserRepository userRepository;
    UserDetailsServiceImpl userDetailsService;
    @Test
    void loadUserByUsername() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
        User user = new User();
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        user.setRole(Role.ROLE_USER);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());

        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertFalse(userDetails.getAuthorities().isEmpty());

        when(userRepository.findByEmail("username")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("username"));


    }
}