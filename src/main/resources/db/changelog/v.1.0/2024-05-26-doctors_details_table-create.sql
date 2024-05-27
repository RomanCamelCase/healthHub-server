create table doctors_details (
     user_id int primary key references users(id),
     is_active boolean not null,
     work_with int not null,
     work_experience date not null,
     qualification int not null,
     description varchar,
     city varchar(64) not null,
     address varchar(128) not null,
     google_maps_place_id varchar
);