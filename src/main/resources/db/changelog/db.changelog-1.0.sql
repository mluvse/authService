--liquibase formatted sql
--changeset mluvse:1
create type role_enum as enum('USER', 'ADMIN');

create table if not exists credentials
(
    id      bigserial primary key,
    user_id bigserial,
    login   varchar(250),
    password_hash VARCHAR(255),
    role role_enum
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_credentials_login ON credentials (login);