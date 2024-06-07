package com.gmail.romkatsis.healthhubserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clinics_contacts")
public class ClinicContact extends Contact{

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    public ClinicContact() {
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }
}
