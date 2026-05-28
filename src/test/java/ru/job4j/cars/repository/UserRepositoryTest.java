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
        var user = createUser(uniqueLogin(), "password");

        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("Find by id returns user")
    void whenFindByIdThenUserPresent() {
        var user = createUser(uniqueLogin(), "password");

        assertThat(repository.findById(user.getId())).isPresent();
    }

    @Test
    @DisplayName("Find by id returns matching login")
    void whenFindByIdThenLoginMatches() {
        var login = uniqueLogin();
        var user = createUser(login, "password");

        assertThat(repository.findById(user.getId()).orElseThrow().getLogin())
                .isEqualTo(login);
    }

    @Test
    @DisplayName("Find by login returns user")
    void whenFindByLoginThenUserPresent() {
        var login = uniqueLogin();
        createUser(login, "password");

        assertThat(repository.findByLogin(login)).isPresent();
    }

    @Test
    @DisplayName("Find by login returns matching id")
    void whenFindByLoginThenIdMatches() {
        var login = uniqueLogin();
        var user = createUser(login, "password");

        assertThat(repository.findByLogin(login).orElseThrow().getId())
                .isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Find by login fragment returns matching user")
    void whenFindByLikeLoginThenContainsUser() {
        var user = createUser(uniqueLogin(), "password");

        assertThat(repository.findByLikeLogin("repository_user_"))
                .extracting(User::getId)
                .contains(user.getId());
    }

    @Test
    @DisplayName("Update changes password")
    void whenUpdateThenPasswordChanged() {
        var user = createUser(uniqueLogin(), "password");
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
        var user = createUser(uniqueLogin(), "password");

        repository.delete(user.getId());

        assertThat(repository.findById(user.getId())).isEmpty();
    }

    private User createUser(String login, String password) {
        var user = new User();
        user.setLogin(login);
        user.setPassword(password);
        repository.create(user);
        var createdUser = repository.findByLogin(login).orElseThrow();
        createdUserIds.add(createdUser.getId());
        return createdUser;
    }

    private String uniqueLogin() {
        return "repository_user_" + System.nanoTime();
    }
}
