package com.dev.foryourwishes.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User with id = %s was not found".formatted(userId));
    }
}
