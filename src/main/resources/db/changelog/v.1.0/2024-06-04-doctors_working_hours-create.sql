create table doctors_working_hours (
    doctor_id int references doctors_details(user_id),
    day_of_week int,
    opening_time time not null,
    closing_time time not null,
    primary key (doctor_id, day_of_week)
);