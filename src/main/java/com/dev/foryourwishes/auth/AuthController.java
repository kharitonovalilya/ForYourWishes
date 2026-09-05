package com.dev.foryourwishes.auth;

import com.dev.foryourwishes.user.RegisterUserRequest;
import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserManagerService;
import com.dev.foryourwishes.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserManagerService userManagerService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody RegisterUserRequest request) {
        User user = userManagerService.create(
                request.email(), request.login(), request.password()
        );
        return new UserResponse(user.getId(), user.getEmail(), user.getLogin());
    }

}
