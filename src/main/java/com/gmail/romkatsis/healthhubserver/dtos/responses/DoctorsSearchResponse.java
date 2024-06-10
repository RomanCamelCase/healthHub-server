package com.gmail.romkatsis.healthhubserver.dtos.responses;

import java.util.Set;

public class DoctorsSearchResponse {

    private Integer totalPages;

    private Integer totalElements;

    private Set<DoctorInfoShortResponse> doctors;

    public DoctorsSearchResponse() {
    }

    public DoctorsSearchResponse(Integer totalPages, Integer totalElements, Set<DoctorInfoShortResponse> doctors) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.doctors = doctors;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Set<DoctorInfoShortResponse> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<DoctorInfoShortResponse> doctors) {
        this.doctors = doctors;
    }
}
