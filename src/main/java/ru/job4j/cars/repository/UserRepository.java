package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.User;

import java.util.Optional;
import java.util.function.Function;

@Component
public class UserRepository {
    private final SessionFactory sf;

    public UserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public User create(User user) {
        return tx(session -> {
            session.persist(user);
            return user;
        });
    }

    public Optional<User> findById(int userId) {
        return tx(session -> Optional.ofNullable(session.find(User.class, userId)));
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        return tx(session -> session.createQuery(
                        "FROM User u WHERE u.email = :email AND u.password = :password",
                        User.class
                )
                .setParameter("email", email)
                .setParameter("password", password)
                .uniqueResultOptional());
    }

    public Optional<User> findByEmail(String email) {
        return tx(session -> session.createQuery(
                        "FROM User u WHERE u.email = :email",
                        User.class
                )
                .setParameter("email", email)
                .uniqueResultOptional());
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
