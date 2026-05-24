package ru.job4j.cars;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Job4jCarsApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void whenApplicationStartsThenLiquibaseCreatesAutoUserAndAutoPostTables() {
        Integer usersCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM auto_user", Integer.class);
        Integer postsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM auto_post", Integer.class);

        assertThat(usersCount).isEqualTo(3);
        assertThat(postsCount).isZero();
    }
}
