create table clinics_amenities (
    clinic_id int references clinics(id),
    amenity_id int references clinics_amenities_types(id),
    primary key (clinic_id, amenity_id)
);