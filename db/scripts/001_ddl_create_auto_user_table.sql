--liquibase formatted sql

--changeset job4j:001-create-auto-user-table
CREATE TABLE auto_user (
    id SERIAL PRIMARY KEY,
    login VARCHAR(128) NOT NULL,
    password VARCHAR(255) NOT NULL
);
--rollback DROP TABLE auto_user;
