package ru.job4j.cars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Job4jCarsApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StandardServiceRegistry registry;

    private SessionFactory sf;

    @BeforeEach
    void initSessionFactory() {
        registry = new StandardServiceRegistryBuilder()
                .configure("hibernate-test.cfg.xml")
                .build();
        sf = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @AfterEach
    void closeSessionFactory() {
        if (sf != null) {
            sf.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    void contextLoads() {
    }

    @Test
    void whenApplicationStartsThenLiquibaseCreatesUsersCarsAndPostsTables() {
        Integer usersCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        Integer carsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cars", Integer.class);
        Integer postsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Integer.class);

        assertThat(usersCount).isZero();
        assertThat(carsCount).isZero();
        assertThat(postsCount).isZero();
    }

    @Test
    void whenSavePostThenSaveUserCarAndPhotoPath() {
        run(session -> {
            var post = new Post();
            post.setTitle("Toyota Camry sale");
            post.setDescription("Car sale post");
            post.setPrice(new BigDecimal("1100000.00"));
            post.setPhotoPath("photos/front.jpg");
            post.setUser(createUser(session));
            post.setCar(createCar(session));

            session.persist(post);
            session.flush();
            session.clear();

            var savedPost = session.find(Post.class, post.getId());

            assertThat(savedPost.getTitle()).isEqualTo("Toyota Camry sale");
            assertThat(savedPost.isSold()).isFalse();
            assertThat(savedPost.getPhotoPath()).isEqualTo("photos/front.jpg");
            assertThat(savedPost.getUser().getEmail()).contains("@example.com");
            assertThat(savedPost.getCar().getBrand()).isEqualTo("Toyota");
        });
    }

    private void run(Consumer<Session> command) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                command.accept(session);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    private User createUser(Session session) {
        var user = new User();
        user.setName("User " + System.nanoTime());
        user.setEmail("user-" + System.nanoTime() + "@example.com");
        user.setPassword("password");
        session.persist(user);
        return user;
    }

    private Car createCar(Session session) {
        var car = new Car();
        car.setBrand("Toyota");
        car.setModel("Camry");
        car.setYear(2020);
        car.setBodyType("sedan");
        car.setEngineType("petrol");
        car.setTransmission("automatic");
        car.setMileage(50_000);
        session.persist(car);
        return car;
    }
}
