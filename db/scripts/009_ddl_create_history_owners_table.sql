create table history_owners(
    id serial primary key,
    car_id int not null references car(id) on delete cascade,
    owner_id int not null references owners(id) on delete cascade,
    start_at timestamp not null,
    end_at timestamp not null,
    unique(car_id, owner_id, start_at)
)