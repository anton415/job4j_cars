package ru.job4j.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends RepositoryTestSupport {
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepository(sf);
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

        assertThat(repository.findByEmail(email)).isPresent();
    }

    @Test
    @DisplayName("Find by email returns matching id")
    void whenFindByEmailThenIdMatches() {
        var email = uniqueEmail();
        var user = createUser(uniqueName(), email, "password");

        assertThat(repository.findByEmail(email).orElseThrow().getId())
                .isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Find by email and password returns matching user")
    void whenFindByEmailAndPasswordThenUserPresent() {
        var email = uniqueEmail();
        var user = createUser(uniqueName(), email, "password");

        assertThat(repository.findByEmailAndPassword(email, "password").orElseThrow().getId())
                .isEqualTo(user.getId());
        assertThat(repository.findByEmailAndPassword(email, "wrong")).isEmpty();
    }

    @Test
    @DisplayName("Create returns empty when email already exists")
    void whenCreateDuplicateEmailThenEmpty() {
        var email = uniqueEmail();
        createUser(uniqueName(), email, "password");
        var user = new User();
        user.setName(uniqueName());
        user.setEmail(email);
        user.setPassword("password");

        assertThat(repository.create(user)).isEmpty();
    }

    private User createUser(String name, String email, String password) {
        var user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        return repository.create(user).orElseThrow();
    }

    private String uniqueName() {
        return "Repository User " + System.nanoTime();
    }

    private String uniqueEmail() {
        return "repository_user_" + System.nanoTime() + "@example.com";
    }
}
