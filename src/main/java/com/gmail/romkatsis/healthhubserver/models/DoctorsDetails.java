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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @MapsId
    private User user;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "doctors_specialisations",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialisation_id")
    )
    private Set<DoctorSpecialisation> specialisations = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "doctors_working_hours",
            joinColumns = @JoinColumn(name = "doctor_id")
    )
    private Set<WorkingDay> workingDays = new HashSet<>();

    @OneToMany(mappedBy = "doctorsDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DoctorContact> contacts = new HashSet<>();

    @ManyToMany(mappedBy = "savedDoctors", fetch = FetchType.LAZY)
    private Set<User> savedByUsers = new HashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DoctorReview> reviews = new HashSet<>();

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

    public Set<DoctorSpecialisation> getSpecialisations() {
        return specialisations;
    }

    public void setSpecialisations(Set<DoctorSpecialisation> specialisations) {
        this.specialisations = specialisations;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
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

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Set<WorkingDay> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Set<WorkingDay> workingDays) {
        this.workingDays = workingDays;
    }

    public Set<DoctorContact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<DoctorContact> contacts) {
        this.contacts = contacts;
    }

    public Set<User> getSavedByUsers() {
        return savedByUsers;
    }

    public void setSavedByUsers(Set<User> savedByUsers) {
        this.savedByUsers = savedByUsers;
    }

    public Set<DoctorReview> getReviews() {
        return reviews;
    }

    public void setReviews(Set<DoctorReview> reviews) {
        this.reviews = reviews;
    }

    public void addSpecialisation(DoctorSpecialisation specialisation) {
        this.specialisations.add(specialisation);
        specialisation.getDoctors().add(this);
    }

    public void removeSpecialisation(DoctorSpecialisation specialisation) {
        this.specialisations.remove(specialisation);
        specialisation.getDoctors().remove(this);
    }

    public void assignSpecialisations(Set<DoctorSpecialisation> specialisations) {
        Set<DoctorSpecialisation> specialisationSet = new HashSet<>(this.specialisations);
        specialisationSet.forEach(this::removeSpecialisation);
        specialisations.forEach(this::addSpecialisation);

    }

    public void addContact(DoctorContact contact) {
        this.contacts.add(contact);
        contact.setDoctorsDetails(this);
    }

    public void removeContactById(int contactId) {
        DoctorContact contact = this.contacts.stream().
                filter(c -> c.getId() == contactId).findFirst().orElse(null);
        if (contact != null) {
            this.contacts.remove(contact);
            contact.setDoctorsDetails(null);
        }
    }
}
