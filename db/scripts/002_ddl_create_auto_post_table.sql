--liquibase formatted sql

--changeset job4j:002-create-auto-post-table
CREATE TABLE auto_post (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    auto_user_id INT NOT NULL REFERENCES auto_user(id)
);
--rollback DROP TABLE auto_post;
