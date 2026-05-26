package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PostRepository {
    private final CrudRepository crudRepository;

    public PostRepository(SessionFactory sf) {
        this(new CrudRepository(sf));
    }

    public PostRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    public void delete(Integer postId) {
        crudRepository.run(session -> {
            var post = session.find(Post.class, postId);
            if (post != null) {
                session.remove(post);
            }
        });
    }

    public Optional<Post> findById(Integer postId) {
        return crudRepository.optional(
                "SELECT p FROM Post p "
                        + "JOIN FETCH p.user "
                        + "JOIN FETCH p.car "
                        + "WHERE p.id = :postId",
                Post.class,
                Map.of("postId", postId)
        );
    }

    public List<Post> findAllLastDay() {
        var createdBefore = LocalDateTime.now();
        return crudRepository.query(
                "SELECT p FROM Post p "
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
        return crudRepository.query(
                "SELECT p FROM Post p "
                        + "JOIN FETCH p.user "
                        + "JOIN FETCH p.car "
                        + "WHERE p.photo IS NOT NULL AND p.photo <> '' "
                        + "ORDER BY p.id",
                Post.class
        );
    }

    public List<Post> findAllByBrand(String brand) {
        return crudRepository.query(
                "SELECT p FROM Post p "
                        + "JOIN FETCH p.user "
                        + "JOIN FETCH p.car c "
                        + "WHERE c.name = :brand "
                        + "ORDER BY p.id",
                Post.class,
                Map.of("brand", brand)
        );
    }
}
