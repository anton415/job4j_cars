package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final SessionFactory sf;

    public UserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь.
     */
    public User create(User user) {
        Transaction transaction = null;
        try (var session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery(
                            "INSERT INTO User (login, password) "
                                    + "VALUES (:login, :password)")
                    .setParameter("login", user.getLogin())
                    .setParameter("password", user.getPassword())
                    .executeUpdate();
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Transaction transaction = null;
        try (var session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery(
                            "UPDATE User u SET u.login = :login, u.password = :password "
                                    + "WHERE u.id = :userId")
                    .setParameter("login", user.getLogin())
                    .setParameter("password", user.getPassword())
                    .setParameter("userId", user.getId())
                    .executeUpdate();
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(Integer userId) {
        Transaction transaction = null;
        try (var session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User u WHERE u.id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        try (var session = sf.openSession()) {
            return session.createQuery("FROM User u ORDER BY u.id", User.class)
                    .list();
        }
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(Integer userId) {
        try (var session = sf.openSession()) {
            return session.createQuery("FROM User u WHERE u.id = :userId", User.class)
                    .setParameter("userId", userId)
                    .uniqueResultOptional();
        }
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        try (var session = sf.openSession()) {
            return session.createQuery(
                            "FROM User u WHERE u.login LIKE :key ORDER BY u.id", User.class)
                    .setParameter("key", "%" + key + "%")
                    .list();
        }
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        try (var session = sf.openSession()) {
            return findByLogin(login, session);
        }
    }

    private Optional<User> findByLogin(String login, Session session) {
        return session.createQuery(
                        "FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", login)
                .uniqueResultOptional();
    }
}
