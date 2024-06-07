create table saved_clinics (
    user_id int references users(id),
    clinic_id int references clinics(id),
    primary key (user_id, clinic_id)
);