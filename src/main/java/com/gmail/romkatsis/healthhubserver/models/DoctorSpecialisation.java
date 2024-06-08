package com.gmail.romkatsis.healthhubserver.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors_specialisations_types")
public class DoctorSpecialisation extends Specialisation {

    @ManyToMany(mappedBy = "specialisations")
    private Set<DoctorsDetails> doctors = new HashSet<>();

    public DoctorSpecialisation() {
    }

    public Set<DoctorsDetails> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<DoctorsDetails> doctors) {
        this.doctors = doctors;
    }
}
