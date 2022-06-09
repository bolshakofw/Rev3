create table role
(
    id   bigint generated by default as identity
        primary key,
    role varchar(255)
);

INSERT INTO role
VALUES (1, 'ROLE_ADMIN');
INSERT INTO role
VALUES (2, 'ROLE_USER');

alter table role
    owner to postgres;