package com.gmail.romkatsis.healthhubserver.dtos.responses;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.SpecialisationDto;

import java.util.Set;

public class ClinicInfoShortResponse {

    private int id;

    private String name;

    private String address;

    private Boolean isPrivate;

    private Double rating;

    private Set<SpecialisationDto> specialisations;

    public ClinicInfoShortResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Set<SpecialisationDto> getSpecialisations() {
        return specialisations;
    }

    public void setSpecialisations(Set<SpecialisationDto> specialisations) {
        this.specialisations = specialisations;
    }
}
