package com.gmail.romkatsis.healthhubserver.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "clinics")
public class Clinic {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @Column(name = "description")
    private String description;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "google_maps_place_id")
    private String googleMapsPlaceId;

    @Column(name = "secret_code", nullable = false, unique = true)
    private String secretCode;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false, unique = true)
    private DoctorsDetails admin;

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<DoctorsDetails> doctors = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "clinics_specialisations",
            joinColumns = @JoinColumn(name = "clinic_id"),
            inverseJoinColumns = @JoinColumn(name = "specialisation_id")
    )
    private Set<ClinicSpecialisation> specialisations = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "clinics_working_hours",
            joinColumns = @JoinColumn(name = "clinic_id")
    )
    private Set<WorkingDay> workingDays = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClinicContact> contacts = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "clinics_amenities",
            joinColumns = @JoinColumn(name = "clinic_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<ClinicAmenity> amenities = new HashSet<>();


    @ManyToMany(mappedBy = "savedClinics", fetch = FetchType.LAZY)
    private Set<User> savedByUsers = new HashSet<>();

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ClinicReview> reviews = new HashSet<>();

    @Formula("(select round(coalesce(avg(r.rating + 0.0), 0), 2) from clinics_reviews r where r.clinic_id = id)")
    private Double avgRating;

    public Clinic() {
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

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
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

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public DoctorsDetails getAdmin() {
        return admin;
    }

    public void setAdmin(DoctorsDetails admin) {
        this.admin = admin;
    }

    public Set<DoctorsDetails> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<DoctorsDetails> doctors) {
        this.doctors = doctors;
    }

    public Set<ClinicSpecialisation> getSpecialisations() {
        return specialisations;
    }

    public void setSpecialisations(Set<ClinicSpecialisation> specialisations) {
        this.specialisations = specialisations;
    }

    public Set<WorkingDay> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Set<WorkingDay> workingDays) {
        this.workingDays = workingDays;
    }

    public Set<ClinicContact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ClinicContact> contacts) {
        this.contacts = contacts;
    }

    public Set<ClinicAmenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<ClinicAmenity> amenities) {
        this.amenities = amenities;
    }

    public Set<User> getSavedByUsers() {
        return savedByUsers;
    }

    public void setSavedByUsers(Set<User> savedByUsers) {
        this.savedByUsers = savedByUsers;
    }

    public Set<ClinicReview> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ClinicReview> reviews) {
        this.reviews = reviews;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public String generateSecretCode() {
        this.secretCode = UUID.randomUUID().toString();
        return this.secretCode;
    }

    public void addDoctor(DoctorsDetails doctor) {
        this.doctors.add(doctor);
        doctor.setClinic(this);
    }

    public void removeDoctor(DoctorsDetails doctor) {
        this.doctors.remove(doctor);
        doctor.setClinic(null);
    }

    public void addSpecialisation(ClinicSpecialisation specialisation) {
        this.specialisations.add(specialisation);
        specialisation.getClinics().add(this);
    }

    public void removeSpecialisation(ClinicSpecialisation specialisation) {
        this.specialisations.remove(specialisation);
        specialisation.getClinics().remove(this);
    }

    public void assignSpecialisations(Set<ClinicSpecialisation> specialisations) {
        Set<ClinicSpecialisation> specialisationSet = new HashSet<>(this.specialisations);
        specialisationSet.forEach(this::removeSpecialisation);
        specialisations.forEach(this::addSpecialisation);
    }

    public void addContact(ClinicContact contact) {
        this.contacts.add(contact);
        contact.setClinic(this);
    }

    public void removeContactById(int contactId) {
        ClinicContact contact = this.contacts.stream().
                filter(c -> c.getId() == contactId).findFirst().orElse(null);
        if (contact != null) {
            this.contacts.remove(contact);
            contact.setClinic(null);
        }
    }

    public void addAmenity(ClinicAmenity amenity) {
        this.amenities.add(amenity);
        amenity.getClinics().add(this);
    }

    public void removeAmenity(ClinicAmenity amenity) {
        this.amenities.remove(amenity);
        amenity.getClinics().remove(this);
    }

    public void assignAmenities(Set<ClinicAmenity> amenities) {
        Set<ClinicAmenity> amenitiesSet = new HashSet<>(this.amenities);
        amenitiesSet.forEach(this::removeAmenity);
        amenities.forEach(this::addAmenity);
    }

    public void addReview(ClinicReview review, User user) {
        this.reviews.add(review);
        review.setClinic(this);
        review.setUser(user);
        user.getClinicReviews().add(review);
    }
}
