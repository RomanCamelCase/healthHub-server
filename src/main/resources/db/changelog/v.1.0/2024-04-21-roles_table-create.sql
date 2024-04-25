create table roles (
    user_id int references users(id),
    role varchar(64),
    primary key (user_id, role)
);