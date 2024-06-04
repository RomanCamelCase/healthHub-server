package com.gmail.romkatsis.healthhubserver.models;

import com.gmail.romkatsis.healthhubserver.enums.DoctorQualificationCategory;
import com.gmail.romkatsis.healthhubserver.enums.PatientType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors_details")
public class DoctorsDetails {

    @Column(name = "user_id")
    @Id
    private Integer userId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @MapsId
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "doctors_specialisations",
            joinColumns = {@JoinColumn(name = "doctor_id")},
            inverseJoinColumns = {@JoinColumn(name = "specialisation_id")})
    private Set<DoctorsSpecialisation> doctorsSpecialisations = new HashSet<>();

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "work_with", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PatientType workWith;

    @Column(name = "work_experience", nullable = false)
    private LocalDate workExperience;

    @Column(name = "qualification", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private DoctorQualificationCategory qualificationCategory;

    @Column(name = "description")
    private String description;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "google_maps_place_id")
    private String googleMapsPlaceId;

    @ManyToMany(mappedBy = "savedDoctors")
    private Set<User> savedByUsers = new HashSet<>();

    public DoctorsDetails() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<DoctorsSpecialisation> getDoctorsSpecialisations() {
        return doctorsSpecialisations;
    }

    public void setDoctorsSpecialisations(Set<DoctorsSpecialisation> doctorsSpecialisations) {
        this.doctorsSpecialisations = doctorsSpecialisations;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public PatientType getWorkWith() {
        return workWith;
    }

    public void setWorkWith(PatientType workWith) {
        this.workWith = workWith;
    }

    public LocalDate getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(LocalDate workExperience) {
        this.workExperience = workExperience;
    }

    public DoctorQualificationCategory getQualificationCategory() {
        return qualificationCategory;
    }

    public void setQualificationCategory(DoctorQualificationCategory qualificationCategory) {
        this.qualificationCategory = qualificationCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoogleMapsPlaceId() {
        return googleMapsPlaceId;
    }

    public void setGoogleMapsPlaceId(String googleMapsPlaceId) {
        this.googleMapsPlaceId = googleMapsPlaceId;
    }

    public Set<User> getSavedByUsers() {
        return savedByUsers;
    }

    public void setSavedByUsers(Set<User> savedByUsers) {
        this.savedByUsers = savedByUsers;
    }

    public void addSpecialisation(DoctorsSpecialisation specialisation) {
        doctorsSpecialisations.add(specialisation);
        specialisation.getDoctors().add(this);
    }

    public void removeSpecialisation(DoctorsSpecialisation specialisation) {
        doctorsSpecialisations.remove(specialisation);
        specialisation.getDoctors().remove(this);
    }
}
