package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CarRepository {
    private final HibernateRepository hibernateRepository;

    @Autowired
    public CarRepository(SessionFactory sf) {
        this(new HibernateRepository(sf));
    }

    public CarRepository(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    public Car create(Car car) {
        hibernateRepository.run(session -> session.persist(car));
        return car;
    }

    public void update(Car car) {
        hibernateRepository.run(session -> session.merge(car));
    }

    public void delete(Integer carId) {
        hibernateRepository.run(
                "DELETE FROM Car c WHERE c.id = :carId",
                Map.of("carId", carId)
        );
    }

    public List<Car> findAllOrderById() {
        return hibernateRepository.query(
                "SELECT DISTINCT c FROM Car c "
                        + "JOIN FETCH c.engine "
                        + "LEFT JOIN FETCH c.owners "
                        + "ORDER BY c.id",
                Car.class
        );
    }

    public Optional<Car> findById(Integer carId) {
        return hibernateRepository.optional(
                "SELECT DISTINCT c FROM Car c "
                        + "JOIN FETCH c.engine "
                        + "LEFT JOIN FETCH c.owners "
                        + "WHERE c.id = :carId",
                Car.class,
                Map.of("carId", carId)
        );
    }

    public List<Car> findByLikeName(String key) {
        return hibernateRepository.query(
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
