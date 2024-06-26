--liquibase formatted sql
--changeset Rustam:create_link_table

CREATE TABLE IF NOT EXISTS link
(
    id_link     BIGSERIAL PRIMARY KEY,
    url         VARCHAR(255)                                       NOT NULL,
    id_chat     BIGINT REFERENCES chat (id) ON DELETE CASCADE      NOT NULL,
    last_update TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    UNIQUE (url, id_chat)
);

CREATE INDEX idx_link_url ON link (url);
