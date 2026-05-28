package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.Car;

import java.util.Optional;
import java.util.function.Function;

@Component
public class CarRepository {
    private final SessionFactory sf;

    public CarRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public Car create(Car car) {
        return tx(session -> {
            session.persist(car);
            return car;
        });
    }

    public Optional<Car> findById(int carId) {
        return tx(session -> Optional.ofNullable(session.find(Car.class, carId)));
    }

    private <T> T tx(Function<Session, T> command) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T result = command.apply(session);
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}
