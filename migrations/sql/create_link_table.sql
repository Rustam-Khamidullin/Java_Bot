--liquibase formatted sql

CREATE TABLE IF NOT EXISTS link
(
    id_link     BIGSERIAL PRIMARY KEY,
    url         VARCHAR(255)                                       NOT NULL,
    id_chat     BIGINT REFERENCES chat (id) ON DELETE CASCADE      NOT NULL,
    last_update TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_link_url ON link (url);
