create table if not exists session
(
    id           uuid primary key default gen_random_uuid(),
    alcoholic_id uuid    not null,
    created_at   bigint  not null,
    is_active    boolean not null default true,
    foreign key (alcoholic_id) references alcoholic
);

create table if not exists session_to_refresh
(
    id         uuid primary key default gen_random_uuid(),
    session_id uuid    not null,
    is_active  boolean not null default true,
    foreign key (session_id) references session
        on delete cascade
);
