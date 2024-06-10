package com.gmail.romkatsis.healthhubserver.dtos.responses;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClinicsSearchResponse {

    private Integer totalPages;

    private Integer totalElements;

    private Set<ClinicInfoShortResponse> clinicInfoShortResponses;

    public ClinicsSearchResponse(int totalPages, int totalElements, LinkedHashSet<ClinicInfoShortResponse> clinicInfoShortResponses) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.clinicInfoShortResponses = clinicInfoShortResponses;
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

    public Set<ClinicInfoShortResponse> getClinicInfoShortResponses() {
        return clinicInfoShortResponses;
    }

    public void setClinicInfoShortResponses(Set<ClinicInfoShortResponse> clinicInfoShortResponses) {
        this.clinicInfoShortResponses = clinicInfoShortResponses;
    }
}
