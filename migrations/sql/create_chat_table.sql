--liquibase formatted sql
--changeset Rustam:create_chat_table

CREATE TABLE IF NOT EXISTS chat
(
    id        BIGSERIAL PRIMARY KEY,
    create_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);
