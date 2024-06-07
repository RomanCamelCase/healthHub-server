package com.gmail.romkatsis.healthhubserver.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clinics_specialisations_types")
public class ClinicSpecialisation extends Specialisation {

    @ManyToMany(mappedBy = "specialisations")
    private Set<Clinic> clinics = new HashSet<>();

    public ClinicSpecialisation() {
    }

    public Set<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(Set<Clinic> clinics) {
        this.clinics = clinics;
    }
}
