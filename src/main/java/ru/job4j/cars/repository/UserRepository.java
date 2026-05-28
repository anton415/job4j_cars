package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserRepository {
    private final HibernateRepository hibernateRepository;

    @Autowired
    public UserRepository(SessionFactory sf) {
        this(new HibernateRepository(sf));
    }

    public UserRepository(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь.
     */
    public User create(User user) {
        hibernateRepository.run(session -> session.persist(user));
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        hibernateRepository.run(session -> session.merge(user));
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(Integer userId) {
        hibernateRepository.run(
                "DELETE FROM User u WHERE u.id = :userId",
                Map.of("userId", userId)
        );
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        return hibernateRepository.query("FROM User u ORDER BY u.id", User.class);
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(Integer userId) {
        return hibernateRepository.optional(
                "FROM User u WHERE u.id = :userId", User.class,
                Map.of("userId", userId)
        );
    }

    /**
     * Список пользователей по имени LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return hibernateRepository.query(
                "FROM User u WHERE u.name LIKE :key ORDER BY u.id", User.class,
                Map.of("key", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по email.
     * @param login email.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        return hibernateRepository.optional(
                "FROM User u WHERE u.email = :login", User.class,
                Map.of("login", login)
        );
    }
}
