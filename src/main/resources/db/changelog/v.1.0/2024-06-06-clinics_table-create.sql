create table clinics (
    id int primary key generated always as identity,
    name varchar(128) not null,
    is_private boolean not null,
    description varchar,
    city varchar(64) not null,
    address varchar(128) not null,
    google_maps_place_id varchar
);