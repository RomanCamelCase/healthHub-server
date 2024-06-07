package com.gmail.romkatsis.healthhubserver.models;

import com.gmail.romkatsis.healthhubserver.enums.Gender;
import com.gmail.romkatsis.healthhubserver.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private DoctorsDetails doctorsDetails;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "saved_doctors",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    private Set<DoctorsDetails> savedDoctors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "saved_clinics",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id")
            )
    private Set<Clinic> savedClinics = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DoctorReview> doctorsReviews = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClinicReview> clinicReviews = new HashSet<>();

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public DoctorsDetails getDoctorsDetails() {
        return doctorsDetails;
    }

    public Set<DoctorsDetails> getSavedDoctors() {
        return savedDoctors;
    }

    public void setSavedDoctors(Set<DoctorsDetails> savedDoctors) {
        this.savedDoctors = savedDoctors;
    }

    public void setDoctorsDetails(DoctorsDetails doctorsDetails) {
        this.doctorsDetails = doctorsDetails;
    }

    public Set<Clinic> getSavedClinics() {
        return savedClinics;
    }

    public void setSavedClinics(Set<Clinic> savedClinics) {
        this.savedClinics = savedClinics;
    }

    public Set<DoctorReview> getDoctorsReviews() {
        return doctorsReviews;
    }

    public void setDoctorsReviews(Set<DoctorReview> doctorsReviews) {
        this.doctorsReviews = doctorsReviews;
    }

    public Set<ClinicReview> getClinicReviews() {
        return clinicReviews;
    }

    public void setClinicReviews(Set<ClinicReview> clinicReviews) {
        this.clinicReviews = clinicReviews;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addDoctorsDetails(DoctorsDetails doctorsDetails) {
        this.doctorsDetails = doctorsDetails;
        doctorsDetails.setUser(this);
    }

    public void saveDoctor(DoctorsDetails savedDoctor) {
        this.savedDoctors.add(savedDoctor);
        savedDoctor.getSavedByUsers().add(this);
    }

    public void removeDoctorFromSaved(DoctorsDetails savedDoctor) {
        this.savedDoctors.remove(savedDoctor);
        savedDoctor.getSavedByUsers().remove(this);
    }

    public void saveClinic(Clinic clinic) {
        this.savedClinics.add(clinic);
        clinic.getSavedByUsers().add(this);
    }

    public void removeClinicFromSaved(Clinic clinic) {
        this.savedClinics.remove(clinic);
        clinic.getSavedByUsers().remove(this);
    }

    public void addDoctorReview(DoctorReview doctorReview) {
        this.doctorsReviews.add(doctorReview);
        doctorReview.getDoctor().getReviews().add(doctorReview);
    }

    public void addClinicReview(ClinicReview clinicReview) {
        this.clinicReviews.add(clinicReview);
        clinicReview.getClinic().getReviews().add(clinicReview);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
