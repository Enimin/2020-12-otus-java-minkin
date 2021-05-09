-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 50;

create table client
(
    id         bigint not null primary key,
    name       varchar(50),
    address_id bigint
);

create table address
(
    id     bigint not null primary key,
    street varchar(50)
);

create table phone
(
    id        bigint not null primary key,
    number    varchar(50),
    client_id bigint
);

create table users
(
    id       bigint not null primary key,
    login    varchar(50),
    password varchar(50)
);

insert into users(id, login, password)
select nextval('hibernate_sequence'), 'admin','admin' union all
select nextval('hibernate_sequence'), 'user','1';

insert into address(id, street)
select nextval('hibernate_sequence'), 'street1' union all
select nextval('hibernate_sequence'), 'street2';


insert into client(id, name, address_id)
select nextval('hibernate_sequence'), 'client1', id from address where street = 'street1' union all
select nextval('hibernate_sequence'), 'client2', id from address where street = 'street2';

insert into phone(id, number, client_id)
select nextval('hibernate_sequence'), '123456',     id from client where name = 'client1' union all
select nextval('hibernate_sequence'), '789012',     id from client where name = 'client1' union all
select nextval('hibernate_sequence'), '01-234-567', id from client where name = 'client2';
