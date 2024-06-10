package com.gmail.romkatsis.healthhubserver.dtos.requests;

import com.gmail.romkatsis.healthhubserver.enums.Gender;
import com.gmail.romkatsis.healthhubserver.enums.PatientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DoctorSearchRequest {

    private Integer pageSize = 10;

    private Integer pageNumber = 0;

    private String sortBy = "rating";

    @NotBlank
    private String city;

    @NotNull
    private Integer specialisationId;

    private PatientType workWith;

    private Integer workExperienceYears = 0;

    private Gender gender;

    private Double minRating;

    public DoctorSearchRequest() {
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public @NotBlank String getCity() {
        return city;
    }

    public void setCity(@NotBlank String city) {
        this.city = city;
    }

    public @NotNull Integer getSpecialisationId() {
        return specialisationId;
    }

    public void setSpecialisationId(@NotNull Integer specialisationId) {
        this.specialisationId = specialisationId;
    }

    public PatientType getWorkWith() {
        return workWith;
    }

    public void setWorkWith(PatientType workWith) {
        this.workWith = workWith;
    }

    public Integer getWorkExperienceYears() {
        return workExperienceYears;
    }

    public void setWorkExperienceYears(Integer workExperienceYears) {
        this.workExperienceYears = workExperienceYears;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }
}
