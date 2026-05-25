--liquibase formatted sql

--changeset job4j:006-create-participates-table
CREATE TABLE participates (
    user_id INT NOT NULL REFERENCES auto_user(id) ON DELETE CASCADE,
    post_id INT NOT NULL REFERENCES auto_post(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, post_id)
);
--rollback DROP TABLE participates;
