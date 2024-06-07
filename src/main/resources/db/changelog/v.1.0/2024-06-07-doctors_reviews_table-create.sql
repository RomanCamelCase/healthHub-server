create table doctors_reviews (
    id int primary key generated always as identity,
    user_id int references users(id) not null,
    doctor_id int references doctors_details(user_id) not null,
    is_anonymous boolean not null,
    rating int not null,
    text varchar(128)
);