package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class PostRepository {
    private final SessionFactory sf;

    public PostRepository(SessionFactory sf) {
        this.sf = sf;
    }

    public Post create(Post post) {
        return tx(session -> {
            session.persist(post);
            return post;
        });
    }

    public List<Post> findAll() {
        return tx(session -> session.createQuery(
                        "SELECT DISTINCT p FROM Post p "
                                + "JOIN FETCH p.user "
                                + "JOIN FETCH p.car "
                                + "ORDER BY p.created DESC, p.id DESC",
                        Post.class
                )
                .list());
    }

    public Optional<Post> findById(int postId) {
        return tx(session -> session.createQuery(
                        "SELECT DISTINCT p FROM Post p "
                                + "JOIN FETCH p.user "
                                + "JOIN FETCH p.car "
                                + "WHERE p.id = :postId",
                        Post.class
                )
                .setParameter("postId", postId)
                .uniqueResultOptional());
    }

    public boolean updateSoldStatus(int postId, boolean sold) {
        return tx(session -> session.createMutationQuery(
                        "UPDATE Post p SET p.sold = :sold WHERE p.id = :postId"
                )
                .setParameter("sold", sold)
                .setParameter("postId", postId)
                .executeUpdate() > 0);
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
