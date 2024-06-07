alter table clinics
    add column admin_id int references doctors_details(user_id) not null unique,
    add column secret_code varchar not null unique;