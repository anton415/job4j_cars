package ru.job4j.cars.repository;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends RepositoryTestSupport {
    private PostRepository postRepository;

    private CarRepository carRepository;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        postRepository = new PostRepository(sf);
        carRepository = new CarRepository(sf);
        userRepository = new UserRepository(sf);
    }

    @Test
    @DisplayName("Find all returns newest posts first with author and car")
    void whenFindAllThenNewestFirstWithAuthorAndCar() {
        var old = createPost("old", LocalDateTime.now().minusDays(2), null, "Toyota");
        var recent = createPost("recent", LocalDateTime.now().minusHours(2), null, "Honda");

        var posts = postRepository.findAll();

        assertThat(posts).extracting(Post::getId)
                .containsSubsequence(recent.getId(), old.getId());
        assertThat(Hibernate.isInitialized(posts.get(0).getUser())).isTrue();
        assertThat(Hibernate.isInitialized(posts.get(0).getCar())).isTrue();
    }

    @Test
    @DisplayName("Find by id returns post with author and car")
    void whenFindByIdThenPostHasAuthorAndCar() {
        var post = createPost("with photo", LocalDateTime.now(), "front.jpg", "Toyota");

        var found = postRepository.findById(post.getId()).orElseThrow();

        assertThat(found.getPhotoPath())
                .isEqualTo("front.jpg");
        assertThat(found.getUser().getEmail()).contains("@example.com");
        assertThat(found.getCar().getBrand()).isEqualTo("Toyota");
        assertThat(Hibernate.isInitialized(found.getUser())).isTrue();
        assertThat(Hibernate.isInitialized(found.getCar())).isTrue();
    }

    @Test
    @DisplayName("Update sold status changes existing post only")
    void whenUpdateSoldStatusThenStatusChanged() {
        var post = createPost("status", LocalDateTime.now(), null, "Toyota");

        assertThat(postRepository.updateSoldStatus(post.getId(), true)).isTrue();
        assertThat(postRepository.findById(post.getId()).orElseThrow().isSold()).isTrue();
        assertThat(postRepository.updateSoldStatus(-1, true)).isFalse();
    }

    private Post createPost(String title, LocalDateTime created, String photoPath, String brand) {
        var post = new Post();
        post.setTitle(title);
        post.setDescription(title + " description");
        post.setPrice(new BigDecimal("1000000.00"));
        post.setCreated(created);
        post.setPhotoPath(photoPath);
        post.setUser(createUser());
        post.setCar(createCar(brand));
        return postRepository.create(post);
    }

    private User createUser() {
        var user = new User();
        user.setName(uniqueName("post_user"));
        user.setEmail(uniqueName("post_user") + "@example.com");
        user.setPassword("password");
        return userRepository.create(user);
    }

    private Car createCar(String brand) {
        var car = new Car();
        car.setBrand(brand);
        car.setModel("model");
        car.setYear(2020);
        car.setBodyType("sedan");
        car.setEngineType("petrol");
        car.setTransmission("automatic");
        car.setMileage(20_000);
        return carRepository.create(car);
    }

    private String uniqueName(String prefix) {
        return prefix + "_" + System.nanoTime();
    }
}
