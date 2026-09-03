package com.dev.foryourwishes.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User with id = %s was not found".formatted(userId));
    }
}
