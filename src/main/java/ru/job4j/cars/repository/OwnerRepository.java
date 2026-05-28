package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OwnerRepository {
    private final HibernateRepository hibernateRepository;

    @Autowired
    public OwnerRepository(SessionFactory sf) {
        this(new HibernateRepository(sf));
    }

    public OwnerRepository(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    public Owner create(Owner owner) {
        hibernateRepository.run(session -> session.persist(owner));
        return owner;
    }

    public void update(Owner owner) {
        hibernateRepository.run(session -> session.merge(owner));
    }

    public void delete(Integer ownerId) {
        hibernateRepository.run(
                "DELETE FROM Owner o WHERE o.id = :ownerId",
                Map.of("ownerId", ownerId)
        );
    }

    public List<Owner> findAllOrderById() {
        return hibernateRepository.query("FROM Owner o ORDER BY o.id", Owner.class);
    }

    public Optional<Owner> findById(Integer ownerId) {
        return hibernateRepository.optional(
                "FROM Owner o WHERE o.id = :ownerId", Owner.class,
                Map.of("ownerId", ownerId)
        );
    }

    public List<Owner> findByLikeName(String key) {
        return hibernateRepository.query(
                "FROM Owner o WHERE o.name LIKE :key ORDER BY o.id", Owner.class,
                Map.of("key", "%" + key + "%")
        );
    }
}
