package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

abstract class HibernateRepositoryTest {
    private StandardServiceRegistry registry;
    private SessionFactory sf;

    protected HibernateRepository hibernateRepository;

    @BeforeEach
    void initSessionFactory() {
        registry = new StandardServiceRegistryBuilder()
                .configure("hibernate-test.cfg.xml")
                .build();
        sf = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
        hibernateRepository = new HibernateRepository(sf);
    }

    @AfterEach
    void closeSessionFactory() {
        if (sf != null) {
            sf.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
