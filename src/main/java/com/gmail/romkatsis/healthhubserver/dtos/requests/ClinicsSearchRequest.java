package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class ClinicsSearchRequest {

    private Integer pageSize = 10;

    private Integer pageNumber = 0;

    private String sortBy = "rating";

    @NotBlank
    private String city;

    @NotNull
    private Integer specialisationId;

    private Set<Integer> amenitiesIds;

    private Boolean isPrivate;

    private Double minRating;

    public ClinicsSearchRequest() {
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String searchBy) {
        this.sortBy = searchBy;
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

    public Set<Integer> getAmenitiesIds() {
        return amenitiesIds;
    }

    public void setAmenitiesIds(Set<Integer> amenitiesIds) {
        this.amenitiesIds = amenitiesIds;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }
}
