--liquibase formatted sql

--changeset job4j:002-create-cars-table
CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    production_year INT NOT NULL,
    body_type VARCHAR(255) NOT NULL,
    engine_type VARCHAR(255) NOT NULL,
    transmission VARCHAR(255) NOT NULL,
    mileage INT DEFAULT 0 NOT NULL
);
--rollback DROP TABLE cars;
