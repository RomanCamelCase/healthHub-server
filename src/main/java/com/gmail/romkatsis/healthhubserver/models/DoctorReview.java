package com.gmail.romkatsis.healthhubserver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors_reviews")
public class DoctorReview extends Review {

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorsDetails doctor;

    public DoctorReview() {
    }

    public DoctorsDetails getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorsDetails doctor) {
        this.doctor = doctor;
    }
}
