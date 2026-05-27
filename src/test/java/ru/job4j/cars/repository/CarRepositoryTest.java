package ru.job4j.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import static org.assertj.core.api.Assertions.assertThat;

class CarRepositoryTest extends HibernateRepositoryTest {
    private CarRepository repository;

    private EngineRepository engineRepository;

    @BeforeEach
    void setUp() {
        repository = new CarRepository(crudRepository);
        engineRepository = new EngineRepository(crudRepository);
    }

    @Test
    void whenCreateThenFindById() {
        var car = car("Toyota");

        repository.create(car);

        assertThat(repository.findById(car.getId()).orElseThrow().getName()).isEqualTo("Toyota");
    }

    @Test
    void whenFindByLikeNameThenReturnsMatch() {
        var car = repository.create(car("Honda"));

        assertThat(repository.findByLikeName("ond")).contains(car);
    }

    private Car car(String name) {
        var engine = new Engine();
        engine.setName("engine_" + System.nanoTime());
        var car = new Car();
        car.setName(name);
        car.setEngine(engineRepository.create(engine));
        return car;
    }
}
