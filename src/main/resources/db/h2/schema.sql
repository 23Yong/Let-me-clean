drop table member if exists;

create table member
(
    id             bigint,
    email          varchar(45) not null,
    password       varchar(60) not null,
    username       varchar(45) not null,
    nickname       varchar(45) not null,
    tel            varchar(45) not null,
    point          int         not null default 0,
    address_code   varchar(45),
    address_detail varchar(45),
    status         varchar(45) not null default 'DEFAULT',
    created_at     timestamp   not null,
    updated_at     timestamp   not null
);