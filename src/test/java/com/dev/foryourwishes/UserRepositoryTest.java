package com.dev.foryourwishes;

import com.dev.foryourwishes.user.User;
import com.dev.foryourwishes.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAndFindUserById() {
        User user = new User(
                "lilya@test.com",
                "lilya",
                12345678L
        );

        User savedUser = userRepository.saveAndFlush(user);

        entityManager.clear();

        User foundUser = userRepository.findById(savedUser.getId())
                .orElseThrow();

        assertThat(foundUser.getEmail())
                .isEqualTo("lilya@test.com");

        assertThat(foundUser.getLogin())
                .isEqualTo("lilya");

        assertThat(foundUser.getPasswordHash())
                .isEqualTo(12345678L);
    }
}
