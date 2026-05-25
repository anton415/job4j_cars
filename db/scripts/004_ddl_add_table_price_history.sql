--liquibase formatted sql

--changeset job4j:005-create-price-history-table
CREATE TABLE price_history (
    id SERIAL PRIMARY KEY,
    before BIGINT NOT NULL,
    after BIGINT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    auto_post_id INT NOT NULL REFERENCES auto_post(id)
);
--rollback DROP TABLE price_history;
