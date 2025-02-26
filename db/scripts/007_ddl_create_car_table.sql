create table car(
    id serial primary key,
    name varchar(25) not null,
    engine_id int not null unique references engine(id)
)
