create table doctors_contacts (
    doctor_id int references doctors_details(user_id),
    contact_id int generated always as identity,
    type varchar(64) not null,
    value varchar,
    primary key (doctor_id, contact_id)
);