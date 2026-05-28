package ru.job4j.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;

import static org.assertj.core.api.Assertions.assertThat;

class CarRepositoryTest extends RepositoryTestSupport {
    private CarRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CarRepository(sf);
    }

    @Test
    void whenCreateThenFindById() {
        var car = car("Toyota", "Camry");

        repository.create(car);

        assertThat(repository.findById(car.getId()).orElseThrow().getBrand()).isEqualTo("Toyota");
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
