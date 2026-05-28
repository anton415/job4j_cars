package ru.job4j.cars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.HibernateRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Job4jCarsApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StandardServiceRegistry registry;

    private SessionFactory sf;

    private HibernateRepository hibernateRepository;

    @BeforeEach
    void initSessionFactory() {
        registry = new StandardServiceRegistryBuilder()
                .configure("hibernate-test.cfg.xml")
                .build();
        sf = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
        hibernateRepository = new HibernateRepository(sf);
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
    void whenApplicationStartsThenLiquibaseCreatesAutoUserAutoPostAndPriceHistoryTables() {
        Integer usersCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM auto_user", Integer.class);
        Integer postsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM auto_post", Integer.class);
        Integer priceHistoryCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM price_history", Integer.class);
        Integer participatesCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM participates", Integer.class);
        Integer enginesCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM engine", Integer.class);
        Integer carsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM car", Integer.class);
        Integer ownersCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM owners", Integer.class);
        Integer historyOwnersCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM history_owners", Integer.class);
        Integer historyCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM history", Integer.class);
        Integer postPhotoCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM auto_post_photo", Integer.class);

        assertThat(usersCount).isEqualTo(3);
        assertThat(postsCount).isZero();
        assertThat(priceHistoryCount).isZero();
        assertThat(participatesCount).isZero();
        assertThat(enginesCount).isZero();
        assertThat(carsCount).isZero();
        assertThat(ownersCount).isZero();
        assertThat(historyOwnersCount).isZero();
        assertThat(historyCount).isZero();
        assertThat(postPhotoCount).isZero();
    }

    @Test
    void whenSavePostThenSavePriceHistory() {
        hibernateRepository.run(session -> {
            var post = new Post();
            post.setDescription("Car sale post");
            post.setUser(createUser(session));
            post.setCar(createCar(session));
            var priceHistory = new PriceHistory();
            priceHistory.setBefore(1_000_000L);
            priceHistory.setAfter(950_000L);
            post.addPriceHistory(priceHistory);

            session.persist(post);
            session.flush();
            session.clear();

            var savedPost = session.find(Post.class, post.getId());

            assertThat(savedPost.getPriceHistory())
                    .extracting(PriceHistory::getBefore)
                    .containsExactly(1_000_000L);
        });
    }

    @Test
    void whenSavePostThenSaveParticipates() {
        hibernateRepository.run(session -> {
            var firstUser = createUser(session);
            var secondUser = createUser(session);
            var post = new Post();
            post.setDescription("Car sale post");
            post.setUser(firstUser);
            post.setCar(createCar(session));
            post.addParticipate(secondUser);

            session.persist(post);
            session.flush();
            session.clear();

            var savedPost = session.find(Post.class, post.getId());

            assertThat(savedPost.getParticipates())
                    .extracting(User::getId)
                    .containsExactly(secondUser.getId());
        });
    }

    @Test
    void whenSaveCarThenSaveOwners() {
        hibernateRepository.run(session -> {
            var engine = new Engine();
            engine.setName("V6");
            session.persist(engine);
            var owner = new Owner();
            owner.setName("Ivan Ivanov");
            owner.setUser(createUser(session));
            var car = new Car();
            car.setName("Toyota Camry");
            car.setEngine(engine);
            car.addOwner(owner);

            session.persist(car);
            session.flush();
            session.clear();

            var savedCar = session.find(Car.class, car.getId());

            assertThat(savedCar.getEngine().getName()).isEqualTo("V6");
            assertThat(savedCar.getOwners())
                    .extracting(Owner::getName)
                    .containsExactly("Ivan Ivanov");
        });
    }

    private User createUser(Session session) {
        var user = new User();
        user.setLogin("user-" + System.nanoTime());
        user.setPassword("password");
        session.persist(user);
        return user;
    }

    private Car createCar(Session session) {
        var engine = new Engine();
        engine.setName("engine-" + System.nanoTime());
        session.persist(engine);
        var car = new Car();
        car.setName("car-" + System.nanoTime());
        car.setEngine(engine);
        session.persist(car);
        return car;
    }
}
