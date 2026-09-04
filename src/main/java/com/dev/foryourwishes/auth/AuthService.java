package com.dev.foryourwishes.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public void login(String login, String password) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(login, password);
        Authentication authentication = authenticationManager.authenticate(authenticationRequest);
        System.out.println(authentication);
    }
}
