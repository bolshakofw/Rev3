create table role
(
    id   bigint generated by default as identity
        primary key,
    role varchar(255)
);



alter table role
    owner to postgres;
