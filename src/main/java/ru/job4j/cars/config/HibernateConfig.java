package ru.job4j.cars.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class HibernateConfig {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory(
            @Value("${app.hibernate.config:hibernate.cfg.xml}") String hibernateConfig
    ) {
        return new Configuration()
                .configure(hibernateConfig)
                .buildSessionFactory();
    }
}
