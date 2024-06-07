create table clinics_specialisations (
    clinic_id int references clinics(id),
    specialisation_id int references clinics_specialisations_types(id),
    primary key (clinic_id, specialisation_id)
);