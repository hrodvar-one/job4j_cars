create table history_owners(
    id serial primary key,
    car_id int not null references car(id),
    owner_id int not null references owners(id),
    unique(car_id, owner_id)
)