package com.gmail.romkatsis.healthhubserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmail.romkatsis.healthhubserver.enums.ContactType;
import jakarta.persistence.*;

@Entity
@Table(name = "doctors_contacts")
public class Contact {

    @Column(name = "contact_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @Column(name = "value")
    private String value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorsDetails doctorsDetails;

    public Contact() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DoctorsDetails getDoctorsDetails() {
        return doctorsDetails;
    }

    public void setDoctorsDetails(DoctorsDetails doctorsDetails) {
        this.doctorsDetails = doctorsDetails;
    }
}
