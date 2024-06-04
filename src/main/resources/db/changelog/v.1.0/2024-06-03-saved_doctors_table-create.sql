create table saved_doctors (
    user_id int references users(id),
    doctor_id int references doctors_details(user_id),
    primary key (user_id, doctor_id)
);