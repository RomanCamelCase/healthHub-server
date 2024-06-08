package com.gmail.romkatsis.healthhubserver.dtos.requests;

import com.gmail.romkatsis.healthhubserver.enums.DoctorQualificationCategory;
import com.gmail.romkatsis.healthhubserver.enums.PatientType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class DoctorInfoRequest {

    @NotNull
    private PatientType workWith;

    @NotNull
    private LocalDate workExperience;

    @NotNull
    private DoctorQualificationCategory qualificationCategory;

    private String description;

    @NotNull
    @Size(min = 2, max = 64)
    private String city;

    @NotNull
    @Size(min = 2, max = 128)
    private String address;

    public DoctorInfoRequest() {
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
}
