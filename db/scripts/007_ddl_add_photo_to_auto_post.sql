--liquibase formatted sql

--changeset job4j:008-create-auto-post-photo-table
CREATE TABLE auto_post_photo (
    post_id INT NOT NULL REFERENCES auto_post(id) ON DELETE CASCADE,
    path VARCHAR(255) NOT NULL,
    PRIMARY KEY (post_id, path)
);
--rollback DROP TABLE auto_post_photo;
