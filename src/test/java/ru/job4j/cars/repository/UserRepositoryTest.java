package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends HibernateRepositoryTest {
    private UserRepository repository;

    private final List<Integer> createdUserIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        repository = new UserRepository(hibernateRepository);
    }

    @AfterEach
    void tearDown() {
        createdUserIds.stream()
                .filter(userId -> userId != null)
                .forEach(repository::delete);
    }

    @Test
    @DisplayName("Create saves user with generated id")
    void whenCreateThenSavedUserHasId() {
        var user = createUser(uniqueName(), uniqueEmail(), "password");

        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("Find by id returns user")
    void whenFindByIdThenUserPresent() {
        var user = createUser(uniqueName(), uniqueEmail(), "password");

        assertThat(repository.findById(user.getId())).isPresent();
    }

    @Test
    @DisplayName("Find by id returns matching email")
    void whenFindByIdThenEmailMatches() {
        var email = uniqueEmail();
        var user = createUser(uniqueName(), email, "password");

        assertThat(repository.findById(user.getId()).orElseThrow().getEmail())
                .isEqualTo(email);
    }

    @Test
    @DisplayName("Find by email returns user")
    void whenFindByEmailThenUserPresent() {
        var email = uniqueEmail();
        createUser(uniqueName(), email, "password");

        assertThat(repository.findByLogin(email)).isPresent();
    }

    @Test
    @DisplayName("Find by email returns matching id")
    void whenFindByEmailThenIdMatches() {
        var email = uniqueEmail();
        var user = createUser(uniqueName(), email, "password");

        assertThat(repository.findByLogin(email).orElseThrow().getId())
                .isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Find by name fragment returns matching user")
    void whenFindByLikeNameThenContainsUser() {
        var user = createUser("Repository User " + System.nanoTime(), uniqueEmail(), "password");

        assertThat(repository.findByLikeLogin("Repository User"))
                .extracting(User::getId)
                .contains(user.getId());
    }

    @Test
    @DisplayName("Update changes password")
    void whenUpdateThenPasswordChanged() {
        var user = createUser(uniqueName(), uniqueEmail(), "password");
        user.setPassword("newPassword");

        repository.update(user);

        assertThat(repository.findById(user.getId()).orElseThrow().getPassword())
                .isEqualTo("newPassword");
    }

    @Test
    @DisplayName("Find all returns users sorted by id")
    void whenFindAllOrderByIdThenResultSorted() {
        assertThat(repository.findAllOrderById())
                .extracting(User::getId)
                .isSorted();
    }

    @Test
    @DisplayName("Delete removes user")
    void whenDeleteThenUserNotFound() {
        var user = createUser(uniqueName(), uniqueEmail(), "password");

        repository.delete(user.getId());

        assertThat(repository.findById(user.getId())).isEmpty();
    }

    private User createUser(String name, String email, String password) {
        var user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        repository.create(user);
        var createdUser = repository.findByLogin(email).orElseThrow();
        createdUserIds.add(createdUser.getId());
        return createdUser;
    }

    private String uniqueName() {
        return "Repository User " + System.nanoTime();
    }

    private String uniqueEmail() {
        return "repository_user_" + System.nanoTime() + "@example.com";
    }
}
