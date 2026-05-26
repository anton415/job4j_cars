package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CarRepository {
    private final CrudRepository crudRepository;

    public CarRepository(SessionFactory sf) {
        this(new CrudRepository(sf));
    }

    public CarRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    public void update(Car car) {
        crudRepository.run(session -> session.merge(car));
    }

    public void delete(Integer carId) {
        crudRepository.run(
                "DELETE FROM Car c WHERE c.id = :carId",
                Map.of("carId", carId)
        );
    }

    public List<Car> findAllOrderById() {
        return crudRepository.query(
                "SELECT DISTINCT c FROM Car c "
                        + "JOIN FETCH c.engine "
                        + "LEFT JOIN FETCH c.owners "
                        + "ORDER BY c.id",
                Car.class
        );
    }

    public Optional<Car> findById(Integer carId) {
        return crudRepository.optional(
                "SELECT DISTINCT c FROM Car c "
                        + "JOIN FETCH c.engine "
                        + "LEFT JOIN FETCH c.owners "
                        + "WHERE c.id = :carId",
                Car.class,
                Map.of("carId", carId)
        );
    }

    public List<Car> findByLikeName(String key) {
        return crudRepository.query(
                "SELECT DISTINCT c FROM Car c "
                        + "JOIN FETCH c.engine "
                        + "LEFT JOIN FETCH c.owners "
                        + "WHERE c.name LIKE :key "
                        + "ORDER BY c.id",
                Car.class,
                Map.of("key", "%" + key + "%")
        );
    }
}
