--liquibase formatted sql

--changeset shrpv:create-table-clients
create table if not exists clients
(
    id serial primary key,
    name varchar(100) not null,
    phone varchar(100) unique not null,
    email varchar(100) unique not null,
    created_at timestamp default now(),
    updated_at timestamp default now()
);

--changeset shrpv:create-table-bookings
create table if not exists bookings (
    id serial primary key,
    client_id bigint not null references clients(id),
    start_time timestamp not null,
    created_at timestamp default now(),
    deleted_at timestamp,
    reason text,
    unique (client_id, start_time)
);
