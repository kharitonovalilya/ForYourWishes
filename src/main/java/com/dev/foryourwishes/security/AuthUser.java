package com.dev.foryourwishes.security;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public final class AuthUser implements UserDetails, CredentialsContainer {
    @Getter
    private final Long id;
    private final String login;
    private String passwordHash;

    public AuthUser(Long id, String login, String passwordHash) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @Override
    public void eraseCredentials() {
        passwordHash = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String toString() {
        return "AuthUser[id=%d, login=%s]".formatted(id, login);
    }
}
