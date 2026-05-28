--liquibase formatted sql

--changeset job4j:003-create-posts-table
CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(12, 2) NOT NULL,
    sold BOOLEAN DEFAULT FALSE NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id INT NOT NULL REFERENCES users(id),
    car_id INT NOT NULL REFERENCES cars(id),
    photo_path VARCHAR(255)
);
--rollback DROP TABLE posts;
