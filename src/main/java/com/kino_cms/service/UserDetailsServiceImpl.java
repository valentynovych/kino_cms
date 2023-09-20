package com.kino_cms.service;

import com.kino_cms.entity.User;
import com.kino_cms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userService;

    @Override
    @Transactional
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("-> start execution method org.springframework.security.core.userdetails.UserDetails loadUserByUsername() by:" + username);
        User user = userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User %s not found in DB", username)
        ));
        log.info("-> exit from method org.springframework.security.core.userdetails.UserDetails loadUserByUsername() with User:" + user);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
