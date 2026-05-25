package ru.job4j.cars;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
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

        assertThat(usersCount).isEqualTo(3);
        assertThat(postsCount).isZero();
        assertThat(priceHistoryCount).isZero();
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
}
