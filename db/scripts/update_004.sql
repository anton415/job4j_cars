--liquibase formatted sql

--changeset job4j:004-insert-auto-users
INSERT INTO auto_user (login, password) VALUES ('Ivanov', 'root');
INSERT INTO auto_user (login, password) VALUES ('Petrov', 'root');
INSERT INTO auto_user (login, password) VALUES ('Sidorov', 'root');
--rollback DELETE FROM auto_user WHERE login IN ('Ivanov', 'Petrov', 'Sidorov') AND password = 'root';
