package com.gmail.romkatsis.healthhubserver.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors_specialisations_types")
public class DoctorsSpecialisation {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(mappedBy = "doctorsSpecialisations")
    private Set<DoctorsDetails> doctors = new HashSet<>();

    public DoctorsSpecialisation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DoctorsDetails> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<DoctorsDetails> doctors) {
        this.doctors = doctors;
    }
}
