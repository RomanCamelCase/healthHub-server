package com.gmail.romkatsis.healthhubserver.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clinics_amenities_types")
public class ClinicAmenity {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "amenities")
    private Set<Clinic> clinics = new HashSet<>();

    public ClinicAmenity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(Set<Clinic> clinics) {
        this.clinics = clinics;
    }
}
