create table owners (
    id serial primary key,
    name varchar(50) not null,
    user_id int not null unique references auto_user(id)
)