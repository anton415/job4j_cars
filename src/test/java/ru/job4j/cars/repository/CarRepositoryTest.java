package ru.job4j.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;

import static org.assertj.core.api.Assertions.assertThat;

class CarRepositoryTest extends HibernateRepositoryTest {
    private CarRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CarRepository(hibernateRepository);
    }

    @Test
    void whenCreateThenFindById() {
        var car = car("Toyota", "Camry");

        repository.create(car);

        assertThat(repository.findById(car.getId()).orElseThrow().getBrand()).isEqualTo("Toyota");
    }

    @Test
    void whenFindByLikeNameThenReturnsMatch() {
        var car = repository.create(car("Honda", "Accord"));

        assertThat(repository.findByLikeName("ond")).contains(car);
    }

    private Car car(String brand, String model) {
        var car = new Car();
        car.setBrand(brand);
        car.setModel(model);
        car.setYear(2021);
        car.setBodyType("sedan");
        car.setEngineType("petrol");
        car.setTransmission("automatic");
        car.setMileage(10_000);
        return car;
    }
}
