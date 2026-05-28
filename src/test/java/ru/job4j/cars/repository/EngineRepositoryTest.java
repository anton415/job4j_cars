package ru.job4j.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;

import static org.assertj.core.api.Assertions.assertThat;

class EngineRepositoryTest extends HibernateRepositoryTest {
    private EngineRepository repository;

    @BeforeEach
    void setUp() {
        repository = new EngineRepository(hibernateRepository);
    }

    @Test
    void whenCreateThenFindByName() {
        var engine = engine("V8");

        repository.create(engine);

        assertThat(repository.findByName("V8")).contains(engine);
    }

    @Test
    void whenDeleteThenEngineNotFound() {
        var engine = repository.create(engine("V6"));

        repository.delete(engine.getId());

        assertThat(repository.findById(engine.getId())).isEmpty();
    }

    private Engine engine(String name) {
        var engine = new Engine();
        engine.setName(name);
        return engine;
    }
}
