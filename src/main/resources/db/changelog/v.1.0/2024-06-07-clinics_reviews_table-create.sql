create table clinics_reviews (
    id int primary key generated always as identity,
    user_id int references users(id) not null,
    clinic_id int references clinics(id) not null,
    is_anonymous boolean not null,
    rating int not null,
    text varchar(128)
);