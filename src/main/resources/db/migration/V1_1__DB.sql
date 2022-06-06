create table user_profileUU
(
    uuid       uuid    not null
        primary key,
    acces      boolean not null,
    email      varchar(255),
    name       varchar(255),
    passchange timestamp,
    password   varchar(255),
    username   varchar(255)
);

alter table user_profile
    owner to postgres;
