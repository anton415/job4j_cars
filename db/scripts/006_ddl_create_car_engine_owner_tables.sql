--liquibase formatted sql

--changeset job4j:007-create-engine-car-owners-history-tables
CREATE TABLE engine (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    engine_id INT NOT NULL REFERENCES engine(id)
);

CREATE TABLE owners (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id INT NOT NULL REFERENCES auto_user(id) ON DELETE CASCADE
);

CREATE TABLE history_owners (
    car_id INT NOT NULL REFERENCES car(id) ON DELETE CASCADE,
    owner_id INT NOT NULL REFERENCES owners(id) ON DELETE CASCADE,
    PRIMARY KEY (car_id, owner_id)
);

CREATE TABLE history (
    id SERIAL PRIMARY KEY,
    start_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_at TIMESTAMP WITHOUT TIME ZONE
);

ALTER TABLE auto_post
    ADD COLUMN car_id INT NOT NULL REFERENCES car(id);
--rollback ALTER TABLE auto_post DROP COLUMN car_id;
--rollback DROP TABLE history;
--rollback DROP TABLE history_owners;
--rollback DROP TABLE owners;
--rollback DROP TABLE car;
--rollback DROP TABLE engine;
