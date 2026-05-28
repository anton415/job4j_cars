package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PostRepository {
    private final HibernateRepository hibernateRepository;

    @Autowired
    public PostRepository(SessionFactory sf) {
        this(new HibernateRepository(sf));
    }

    public PostRepository(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    public Post create(Post post) {
        hibernateRepository.run(session -> session.persist(post));
        return post;
    }

    public void update(Post post) {
        hibernateRepository.run(session -> session.merge(post));
    }

    public void delete(Integer postId) {
        hibernateRepository.run(session -> {
            var post = session.find(Post.class, postId);
            if (post != null) {
                session.remove(post);
            }
        });
    }

    public Optional<Post> findById(Integer postId) {
        return hibernateRepository.optional(
                "SELECT DISTINCT p FROM Post p "
                        + "JOIN FETCH p.user "
                        + "JOIN FETCH p.car "
                        + "WHERE p.id = :postId",
                Post.class,
                Map.of("postId", postId)
        );
    }

    public List<Post> findAllLastDay() {
        var createdBefore = LocalDateTime.now();
        return hibernateRepository.query(
                "SELECT DISTINCT p FROM Post p "
                        + "JOIN FETCH p.user "
                        + "JOIN FETCH p.car "
                        + "WHERE p.created BETWEEN :createdAfter AND :createdBefore "
                        + "ORDER BY p.id",
                Post.class,
                Map.of(
                        "createdAfter", createdBefore.minusDays(1),
                        "createdBefore", createdBefore
                )
        );
    }

    public List<Post> findAllWithPhoto() {
        return hibernateRepository.query(
                "SELECT DISTINCT p FROM Post p "
                        + "JOIN FETCH p.user "
                        + "JOIN FETCH p.car "
                        + "WHERE p.photoPath IS NOT NULL AND p.photoPath <> '' "
                        + "ORDER BY p.id",
                Post.class
        );
    }

    public List<Post> findAllByBrand(String brand) {
        return hibernateRepository.query(
                "SELECT DISTINCT p FROM Post p "
                        + "JOIN FETCH p.user "
                        + "JOIN FETCH p.car c "
                        + "WHERE c.brand = :brand "
                        + "ORDER BY p.id",
                Post.class,
                Map.of("brand", brand)
        );
    }
}
