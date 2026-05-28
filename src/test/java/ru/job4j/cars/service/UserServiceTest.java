package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void whenRegisterDuplicateEmailThenEmptyAndUserNotCreated() {
        var user = user("new-user@example.com", "password");
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user("new-user@example.com", "old-password")));

        var result = userService.register(user);

        assertThat(result).isEmpty();
        verify(userRepository, never()).create(user);
    }

    @Test
    void whenLoginWithValidCredentialsThenUserReturned() {
        var user = user("user@example.com", "password");
        when(userRepository.findByEmailAndPassword("user@example.com", "password"))
                .thenReturn(Optional.of(user));

        var result = userService.login("user@example.com", "password");

        assertThat(result).contains(user);
    }

    @Test
    void whenLoginWithInvalidCredentialsThenEmpty() {
        when(userRepository.findByEmailAndPassword("user@example.com", "wrong"))
                .thenReturn(Optional.empty());

        var result = userService.login("user@example.com", "wrong");

        assertThat(result).isEmpty();
    }

    private User user(String email, String password) {
        var user = new User();
        user.setName("User");
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
