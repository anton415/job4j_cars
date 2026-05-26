package ru.job4j.cars.repository;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private CrudRepository crudRepository;

    private PostRepository postRepository;

    private CarRepository carRepository;

    private EngineRepository engineRepository;

    private UserRepository userRepository;

    private final List<Integer> createdPostIds = new ArrayList<>();

    private final List<Integer> createdCarIds = new ArrayList<>();

    private final List<Integer> createdEngineIds = new ArrayList<>();

    private final List<Integer> createdUserIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        var sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        crudRepository = new CrudRepository(sessionFactory);
        postRepository = new PostRepository(crudRepository);
        carRepository = new CarRepository(crudRepository);
        engineRepository = new EngineRepository(crudRepository);
        userRepository = new UserRepository(crudRepository);
    }

    @AfterEach
    void tearDown() {
        createdPostIds.stream()
                .filter(Objects::nonNull)
                .forEach(postRepository::delete);
        createdCarIds.stream()
                .filter(Objects::nonNull)
                .forEach(carRepository::delete);
        createdEngineIds.stream()
                .filter(Objects::nonNull)
                .forEach(engineRepository::delete);
        createdUserIds.stream()
                .filter(Objects::nonNull)
                .forEach(userRepository::delete);
    }

    @Test
    @DisplayName("Find all for last day returns recent posts only")
    void whenFindAllForLastDayThenReturnsRecentPostsOnly() {
        var carName = uniqueName("last_day_car");
        var recent = createPost("recent", LocalDateTime.now().minusHours(2), Set.of(), carName);
        var old = createPost("old", LocalDateTime.now().minusDays(2), Set.of(), carName);

        assertThat(postRepository.findAllLastDay())
                .extracting(Post::getId)
                .contains(recent.getId())
                .doesNotContain(old.getId());
    }

    @Test
    @DisplayName("Find all with photo returns posts with photo only")
    void whenFindAllWithPhotoThenReturnsPostsWithPhotoOnly() {
        var withPhoto = createPost(
                "with photo",
                LocalDateTime.now(),
                Set.of("front.jpg", "back.jpg"),
                uniqueName("photo_car")
        );
        var withoutPhoto = createPost("without photo", LocalDateTime.now(), Set.of(), uniqueName("photo_car"));
        var withEmptyPhoto = createPost("with empty photo", LocalDateTime.now(), Set.of(""), uniqueName("photo_car"));

        assertThat(postRepository.findAllWithPhoto())
                .extracting(Post::getId)
                .contains(withPhoto.getId())
                .doesNotContain(withoutPhoto.getId(), withEmptyPhoto.getId());
        assertThat(postRepository.findById(withPhoto.getId()).orElseThrow().getPhotos())
                .containsExactlyInAnyOrder("front.jpg", "back.jpg");
    }

    @Test
    @DisplayName("Find all by car brand returns matching posts only")
    void whenFindAllByCarBrandThenReturnsMatchingPostsOnly() {
        var brand = uniqueName("brand");
        var matching = createPost("matching", LocalDateTime.now(), Set.of(), brand);
        var other = createPost("other", LocalDateTime.now(), Set.of(), uniqueName("brand"));

        assertThat(postRepository.findAllByBrand(brand))
                .extracting(Post::getId)
                .contains(matching.getId())
                .doesNotContain(other.getId());
    }

    private Post createPost(String description, LocalDateTime created, Set<String> photos, String carName) {
        var postRef = new AtomicReference<Post>();
        crudRepository.run(session -> {
            var user = new User();
            user.setLogin(uniqueName("post_user"));
            user.setPassword("password");
            session.persist(user);

            var engine = new Engine();
            engine.setName(uniqueName("engine"));
            session.persist(engine);

            var car = new Car();
            car.setName(carName);
            car.setEngine(engine);
            session.persist(car);

            var post = new Post();
            post.setDescription(description);
            post.setCreated(created);
            post.getPhotos().addAll(photos);
            post.setUser(user);
            post.setCar(car);
            session.persist(post);
            session.flush();

            createdUserIds.add(user.getId());
            createdEngineIds.add(engine.getId());
            createdCarIds.add(car.getId());
            createdPostIds.add(post.getId());
            postRef.set(post);
        });
        return postRef.get();
    }

    private String uniqueName(String prefix) {
        return prefix + "_" + System.nanoTime();
    }
}
