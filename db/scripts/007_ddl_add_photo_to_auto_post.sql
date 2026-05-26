--liquibase formatted sql

--changeset job4j:008-add-photo-to-auto-post
ALTER TABLE auto_post
    ADD COLUMN photo VARCHAR(255);
--rollback ALTER TABLE auto_post DROP COLUMN photo;
