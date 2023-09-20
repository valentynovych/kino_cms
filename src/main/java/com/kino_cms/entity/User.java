package com.kino_cms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kino_cms.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class User implements org.springframework.security.core.userdetails.UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usrname;
    private String email;
    private String password;
    private String createTime;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserDetails userDetails;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserSession> userSessionList;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(20) default 'ROLE_USER'")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
