-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 50;

create table client
(
    id   bigint not null primary key default nextval('hibernate_sequence'),
    name varchar(50)
);

create table address
(
    id        bigint not null primary key default nextval('hibernate_sequence'),
    street    varchar(50),
    client_id bigint,
    FOREIGN KEY (client_id) REFERENCES client
);

create table phone
(
    id        bigint not null primary key default nextval('hibernate_sequence'),
    number    varchar(50),
    client_id bigint,
    FOREIGN KEY (client_id) REFERENCES client (id)
);

insert into client(name)
select 'client1' union all
select 'client2';

insert into address(street, client_id)
select 'street1', id from client where name = 'client1' union all
select 'street2', id from client where name = 'client2';

insert into phone(number, client_id)
select '123456',     id from client where name = 'client1' union all
select '789012',     id from client where name = 'client1' union all
select '01-234-567', id from client where name = 'client2';
