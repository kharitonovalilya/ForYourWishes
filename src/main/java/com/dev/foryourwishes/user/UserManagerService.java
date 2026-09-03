package com.dev.foryourwishes.user;

import com.dev.foryourwishes.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserManagerService {

    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    public User create(String email, String login, String passwordHash) {
        User user = new User(email, login, passwordHash);
        return userRepository.save(user);
    }
}
