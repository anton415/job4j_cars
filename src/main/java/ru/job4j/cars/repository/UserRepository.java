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
     * @return пользователь с id.
     */
    public User create(User user) {
        Transaction transaction = null;
        try (var session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery(
                            "insert into User (login, password) "
                                    + "values (:login, :password)")
                    .setParameter("login", user.getLogin())
                    .setParameter("password", user.getPassword())
                    .executeUpdate();
            findByLogin(user.getLogin(), session)
                    .ifPresent(createdUser -> user.setId(createdUser.getId()));
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
                            "update User u set u.login = :login, u.password = :password "
                                    + "where u.id = :userId")
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
            session.createMutationQuery("delete from User u where u.id = :userId")
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
            return session.createQuery("from User u order by u.id", User.class)
                    .list();
        }
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(Integer userId) {
        try (var session = sf.openSession()) {
            return session.createQuery("from User u where u.id = :userId", User.class)
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
                            "from User u where u.login like :key order by u.id", User.class)
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
                        "from User u where u.login = :login", User.class)
                .setParameter("login", login)
                .uniqueResultOptional();
    }
}
