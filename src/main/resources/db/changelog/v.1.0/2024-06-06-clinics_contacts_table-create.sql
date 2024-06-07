create table clinics_contacts (
    contact_id int primary key generated always as identity,
    clinic_id int references clinics(id) not null,
    type varchar(64) not null,
    value varchar not null
);