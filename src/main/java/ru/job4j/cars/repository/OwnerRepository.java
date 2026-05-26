package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OwnerRepository {
    private final CrudRepository crudRepository;

    public OwnerRepository(SessionFactory sf) {
        this(new CrudRepository(sf));
    }

    public OwnerRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Owner create(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    public void update(Owner owner) {
        crudRepository.run(session -> session.merge(owner));
    }

    public void delete(Integer ownerId) {
        crudRepository.run(
                "DELETE FROM Owner o WHERE o.id = :ownerId",
                Map.of("ownerId", ownerId)
        );
    }

    public List<Owner> findAllOrderById() {
        return crudRepository.query("FROM Owner o ORDER BY o.id", Owner.class);
    }

    public Optional<Owner> findById(Integer ownerId) {
        return crudRepository.optional(
                "FROM Owner o WHERE o.id = :ownerId", Owner.class,
                Map.of("ownerId", ownerId)
        );
    }

    public List<Owner> findByLikeName(String key) {
        return crudRepository.query(
                "FROM Owner o WHERE o.name LIKE :key ORDER BY o.id", Owner.class,
                Map.of("key", "%" + key + "%")
        );
    }
}
