create table doctors_specialisations (
    doctor_id int references doctors_details(user_id),
    specialisation_id int references doctors_specialisations_types(id),
    primary key (doctor_id, specialisation_id)
);