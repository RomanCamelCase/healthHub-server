package com.gmail.romkatsis.healthhubserver.dtos.responses;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.ContactDto;
import com.gmail.romkatsis.healthhubserver.dtos.embedded.SpecialisationDto;
import com.gmail.romkatsis.healthhubserver.dtos.embedded.WorkingDayDto;
import com.gmail.romkatsis.healthhubserver.enums.DoctorQualificationCategory;
import com.gmail.romkatsis.healthhubserver.enums.Gender;
import com.gmail.romkatsis.healthhubserver.enums.PatientType;

import java.time.LocalDate;
import java.util.Set;

public class DoctorInfoResponse {

    private int id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private LocalDate registrationDate;

    private boolean isActive;

    private PatientType workWith;

    private LocalDate workExperience;

    private DoctorQualificationCategory qualificationCategory;

    private String description;

    private String city;

    private String address;

    private String googleMapsPlaceId;

    private Integer clinicId;

    private Set<SpecialisationDto> specialisations;

    private Set<WorkingDayDto> workingDays;

    private Set<ContactDto> contacts;

    public DoctorInfoResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
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

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Set<SpecialisationDto> getSpecialisations() {
        return specialisations;
    }

    public void setSpecialisations(Set<SpecialisationDto> specialisations) {
        this.specialisations = specialisations;
    }

    public Set<WorkingDayDto> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Set<WorkingDayDto> workingDays) {
        this.workingDays = workingDays;
    }

    public Set<ContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ContactDto> contacts) {
        this.contacts = contacts;
    }
}
