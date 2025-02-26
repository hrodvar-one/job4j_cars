create table history(
    id serial primary key,
    start_at timestamp not null default now(),
    end_at timestamp not null default now()
)