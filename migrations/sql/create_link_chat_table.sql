--liquibase formatted sql

CREATE TABLE IF NOT EXISTS chat_link
(
    id_chat BIGINT NOT NULL REFERENCES chat (id) ON DELETE CASCADE,
    id_link BIGINT NOT NULL REFERENCES link (id) ON DELETE CASCADE,

    PRIMARY KEY (id_chat, id_link)
);
