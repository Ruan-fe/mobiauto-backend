package com.mobiautobackend.domain.entities;

import com.mobiautobackend.domain.enumeration.MemberRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "MEMBER")
public class Member implements UserDetails {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(name = "CREATION_DATE", nullable = false)
    private ZonedDateTime creationDate;

    public Member() {
    }

    public Member(String id) {
        this.id = id;
    }

    @PrePersist
    private void prePersist() {
        id = UUID.randomUUID().toString();
        creationDate = ZonedDateTime.now();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final String ROLE_USER = "ROLE_USER";
        if (Objects.equals(this.role, MemberRole.ADMIN)) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (Objects.equals(this.role, MemberRole.MANAGER)) {
            return List.of(new SimpleGrantedAuthority("ROLE_MANAGER"), new SimpleGrantedAuthority(ROLE_USER));
        } else if (Objects.equals(this.role, MemberRole.ASSISTANT)) {
            return List.of(new SimpleGrantedAuthority("ROLE_ASSISTANT"), new SimpleGrantedAuthority(ROLE_USER));
        } else if (Objects.equals(this.role, MemberRole.OWNER)) {
            return List.of(new SimpleGrantedAuthority("ROLE_OWNER"), new SimpleGrantedAuthority(ROLE_USER));
        } else {
            return List.of(new SimpleGrantedAuthority(ROLE_USER));
        }
    }

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

    public void setPassword(String password) {
        this.password = password;
    }

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
