package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EngineRepository {
    private final HibernateRepository hibernateRepository;

    public EngineRepository(SessionFactory sf) {
        this(new HibernateRepository(sf));
    }

    public EngineRepository(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    public Engine create(Engine engine) {
        hibernateRepository.run(session -> session.persist(engine));
        return engine;
    }

    public void update(Engine engine) {
        hibernateRepository.run(session -> session.merge(engine));
    }

    public void delete(Integer engineId) {
        hibernateRepository.run(
                "DELETE FROM Engine e WHERE e.id = :engineId",
                Map.of("engineId", engineId)
        );
    }

    public List<Engine> findAllOrderById() {
        return hibernateRepository.query("FROM Engine e ORDER BY e.id", Engine.class);
    }

    public Optional<Engine> findById(Integer engineId) {
        return hibernateRepository.optional(
                "FROM Engine e WHERE e.id = :engineId", Engine.class,
                Map.of("engineId", engineId)
        );
    }

    public List<Engine> findByLikeName(String key) {
        return hibernateRepository.query(
                "FROM Engine e WHERE e.name LIKE :key ORDER BY e.id", Engine.class,
                Map.of("key", "%" + key + "%")
        );
    }

    public Optional<Engine> findByName(String name) {
        return hibernateRepository.optional(
                "FROM Engine e WHERE e.name = :name", Engine.class,
                Map.of("name", name)
        );
    }
}
