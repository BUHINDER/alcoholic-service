create table alcoholic
(
    id        uuid primary key default gen_random_uuid(),
    firstname varchar not null,
    last_name varchar not null,
    age       integer,
    login     varchar not null,
    password  varchar not null
)
