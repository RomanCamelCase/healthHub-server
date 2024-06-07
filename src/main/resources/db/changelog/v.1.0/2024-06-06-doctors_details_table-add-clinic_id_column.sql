alter table doctors_details
    add column clinic_id int references clinics(id);