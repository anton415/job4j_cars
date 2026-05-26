package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EngineRepository {
    private final CrudRepository crudRepository;

    public EngineRepository(SessionFactory sf) {
        this(new CrudRepository(sf));
    }

    public EngineRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Engine create(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    public void update(Engine engine) {
        crudRepository.run(session -> session.merge(engine));
    }

    public void delete(Integer engineId) {
        crudRepository.run(
                "DELETE FROM Engine e WHERE e.id = :engineId",
                Map.of("engineId", engineId)
        );
    }

    public List<Engine> findAllOrderById() {
        return crudRepository.query("FROM Engine e ORDER BY e.id", Engine.class);
    }

    public Optional<Engine> findById(Integer engineId) {
        return crudRepository.optional(
                "FROM Engine e WHERE e.id = :engineId", Engine.class,
                Map.of("engineId", engineId)
        );
    }

    public List<Engine> findByLikeName(String key) {
        return crudRepository.query(
                "FROM Engine e WHERE e.name LIKE :key ORDER BY e.id", Engine.class,
                Map.of("key", "%" + key + "%")
        );
    }

    public Optional<Engine> findByName(String name) {
        return crudRepository.optional(
                "FROM Engine e WHERE e.name = :name", Engine.class,
                Map.of("name", name)
        );
    }
}
