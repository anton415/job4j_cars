package ru.job4j.cars;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Job4jCarsApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

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
    @Transactional
    void whenSavePostThenSavePriceHistory() {
        var user = entityManager.createQuery("FROM User u ORDER BY u.id", User.class)
                .setMaxResults(1)
                .getSingleResult();
        var post = new Post();
        post.setDescription("Car sale post");
        post.setUser(user);
        post.setCar(createCar());
        var priceHistory = new PriceHistory();
        priceHistory.setBefore(1_000_000L);
        priceHistory.setAfter(950_000L);
        post.addPriceHistory(priceHistory);

        entityManager.persist(post);
        entityManager.flush();
        entityManager.clear();

        var savedPost = entityManager.find(Post.class, post.getId());

        assertThat(savedPost.getPriceHistory())
                .extracting(PriceHistory::getBefore)
                .containsExactly(1_000_000L);
    }

    @Test
    @Transactional
    void whenSavePostThenSaveParticipates() {
        var users = entityManager.createQuery("FROM User u ORDER BY u.id", User.class)
                .setMaxResults(2)
                .getResultList();
        var post = new Post();
        post.setDescription("Car sale post");
        post.setUser(users.get(0));
        post.setCar(createCar());
        post.addParticipate(users.get(1));

        entityManager.persist(post);
        entityManager.flush();
        entityManager.clear();

        var savedPost = entityManager.find(Post.class, post.getId());

        assertThat(savedPost.getParticipates())
                .extracting(User::getId)
                .containsExactly(users.get(1).getId());
    }

    @Test
    @Transactional
    void whenSaveCarThenSaveOwners() {
        var user = entityManager.createQuery("FROM User u ORDER BY u.id", User.class)
                .setMaxResults(1)
                .getSingleResult();
        var engine = new Engine();
        engine.setName("V6");
        entityManager.persist(engine);
        var owner = new Owner();
        owner.setName("Ivan Ivanov");
        owner.setUser(user);
        var car = new Car();
        car.setName("Toyota Camry");
        car.setEngine(engine);
        car.addOwner(owner);

        entityManager.persist(car);
        entityManager.flush();
        entityManager.clear();

        var savedCar = entityManager.find(Car.class, car.getId());

        assertThat(savedCar.getEngine().getName()).isEqualTo("V6");
        assertThat(savedCar.getOwners())
                .extracting(Owner::getName)
                .containsExactly("Ivan Ivanov");
    }

    private Car createCar() {
        var engine = new Engine();
        engine.setName("engine-" + System.nanoTime());
        entityManager.persist(engine);
        var car = new Car();
        car.setName("car-" + System.nanoTime());
        car.setEngine(engine);
        entityManager.persist(car);
        return car;
    }
}
