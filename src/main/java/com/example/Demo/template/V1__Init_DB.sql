create table file_data
(
    uuid        uuid not null
        primary key,
    bytes       bytea,
    change_time timestamp,
    file_name   varchar(255),
    file_type   varchar(255),
    load_time   timestamp,
    size        bigint
);