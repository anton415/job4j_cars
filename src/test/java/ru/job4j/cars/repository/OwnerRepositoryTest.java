package ru.job4j.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerRepositoryTest extends HibernateRepositoryTest {
    private OwnerRepository repository;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        repository = new OwnerRepository(crudRepository);
        userRepository = new UserRepository(crudRepository);
    }

    @Test
    void whenCreateThenFindById() {
        var owner = owner("Ivan");

        repository.create(owner);

        assertThat(repository.findById(owner.getId()).orElseThrow().getName()).isEqualTo("Ivan");
    }

    @Test
    void whenFindByLikeNameThenReturnsMatch() {
        var owner = repository.create(owner("Petr"));

        assertThat(repository.findByLikeName("et")).contains(owner);
    }

    private Owner owner(String name) {
        var owner = new Owner();
        owner.setName(name);
        owner.setUser(userRepository.create(user()));
        return owner;
    }

    private User user() {
        var user = new User();
        user.setLogin("user_" + System.nanoTime());
        user.setPassword("password");
        return user;
    }
}
