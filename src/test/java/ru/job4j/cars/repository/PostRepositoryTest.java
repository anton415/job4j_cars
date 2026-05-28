package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends HibernateRepositoryTest {
    private PostRepository postRepository;

    private CarRepository carRepository;

    private UserRepository userRepository;

    private final List<Integer> createdPostIds = new ArrayList<>();

    private final List<Integer> createdCarIds = new ArrayList<>();

    private final List<Integer> createdUserIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        postRepository = new PostRepository(hibernateRepository);
        carRepository = new CarRepository(hibernateRepository);
        userRepository = new UserRepository(hibernateRepository);
    }

    @AfterEach
    void tearDown() {
        createdPostIds.stream()
                .filter(Objects::nonNull)
                .forEach(postRepository::delete);
        createdCarIds.stream()
                .filter(Objects::nonNull)
                .forEach(carRepository::delete);
        createdUserIds.stream()
                .filter(Objects::nonNull)
                .forEach(userRepository::delete);
    }

    @Test
    @DisplayName("Find all for last day returns recent posts only")
    void whenFindAllForLastDayThenReturnsRecentPostsOnly() {
        var carName = uniqueName("last_day_car");
        var recent = createPost("recent", LocalDateTime.now().minusHours(2), null, carName);
        var old = createPost("old", LocalDateTime.now().minusDays(2), null, carName);

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
                "front.jpg",
                uniqueName("photo_car")
        );
        var withoutPhoto = createPost("without photo", LocalDateTime.now(), null, uniqueName("photo_car"));
        var withEmptyPhoto = createPost("with empty photo", LocalDateTime.now(), "", uniqueName("photo_car"));

        assertThat(postRepository.findAllWithPhoto())
                .extracting(Post::getId)
                .contains(withPhoto.getId())
                .doesNotContain(withoutPhoto.getId(), withEmptyPhoto.getId());
        assertThat(postRepository.findById(withPhoto.getId()).orElseThrow().getPhotoPath())
                .isEqualTo("front.jpg");
    }

    @Test
    @DisplayName("Find all by car brand returns matching posts only")
    void whenFindAllByCarBrandThenReturnsMatchingPostsOnly() {
        var brand = uniqueName("brand");
        var matching = createPost("matching", LocalDateTime.now(), null, brand);
        var other = createPost("other", LocalDateTime.now(), null, uniqueName("brand"));

        assertThat(postRepository.findAllByBrand(brand))
                .extracting(Post::getId)
                .contains(matching.getId())
                .doesNotContain(other.getId());
    }

    private Post createPost(String title, LocalDateTime created, String photoPath, String brand) {
        var postRef = new AtomicReference<Post>();
        hibernateRepository.run(session -> {
            var user = new User();
            user.setName(uniqueName("post_user"));
            user.setEmail(uniqueName("post_user") + "@example.com");
            user.setPassword("password");
            session.persist(user);

            var car = new Car();
            car.setBrand(brand);
            car.setModel("model");
            car.setYear(2020);
            car.setBodyType("sedan");
            car.setEngineType("petrol");
            car.setTransmission("automatic");
            car.setMileage(20_000);
            session.persist(car);

            var post = new Post();
            post.setTitle(title);
            post.setDescription(title + " description");
            post.setPrice(new BigDecimal("1000000.00"));
            post.setCreated(created);
            post.setPhotoPath(photoPath);
            post.setUser(user);
            post.setCar(car);
            session.persist(post);
            session.flush();

            createdUserIds.add(user.getId());
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
