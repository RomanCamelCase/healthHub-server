create table clinics_working_hours (
    clinic_id int references clinics(id),
    day_of_week int,
    opening_time time not null,
    closing_time time not null,
    primary key (clinic_id, day_of_week)
);