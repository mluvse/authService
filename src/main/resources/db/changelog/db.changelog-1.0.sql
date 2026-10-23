--liquibase formatted sql
--changeset mluvse:1
create type role_enum as enum('USER', 'ADMIN');

CREATE TABLE IF NOT EXISTS credentials (
                                           id BIGSERIAL PRIMARY KEY,
                                           login VARCHAR(255) NOT NULL UNIQUE,
                                           password_hash VARCHAR(255) NOT NULL,
                                           user_id BIGINT NOT NULL,
                                           role VARCHAR(50) NOT NULL,
                                           active BOOLEAN DEFAULT true NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_credentials_login ON credentials (login);