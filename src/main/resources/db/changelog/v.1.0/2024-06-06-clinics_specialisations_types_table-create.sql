create table clinics_specialisations_types (
    id int primary key generated by default as identity,
    title varchar(64) not null unique,
    description varchar not null
);