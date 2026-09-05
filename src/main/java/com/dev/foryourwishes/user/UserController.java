package com.dev.foryourwishes.user;

import com.dev.foryourwishes.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagerService userManagerService;

    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal AuthUser currentUser) {
        User user = userManagerService.findById(currentUser.getId());
        return new UserResponse(user.getId(), user.getEmail(), user.getLogin());
    }

}
