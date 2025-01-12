create table author
(
    id              serial primary key,
    name            varchar(100)  not null,
    creationDate    timestamp not null
);