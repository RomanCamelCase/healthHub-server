package com.gmail.romkatsis.healthhubserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors_contacts")
public class DoctorsContact extends Contact{

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorsDetails doctorsDetails;

    public DoctorsContact() {
    }

    public DoctorsDetails getDoctorsDetails() {
        return doctorsDetails;
    }

    public void setDoctorsDetails(DoctorsDetails doctorsDetails) {
        this.doctorsDetails = doctorsDetails;
    }
}
