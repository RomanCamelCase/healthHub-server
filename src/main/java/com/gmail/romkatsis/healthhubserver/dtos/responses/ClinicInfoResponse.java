package com.gmail.romkatsis.healthhubserver.dtos.responses;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.ClinicAmenityDto;
import com.gmail.romkatsis.healthhubserver.dtos.embedded.ContactDto;
import com.gmail.romkatsis.healthhubserver.dtos.embedded.SpecialisationDto;
import com.gmail.romkatsis.healthhubserver.dtos.embedded.WorkingDayDto;

import java.util.Set;

public class ClinicInfoResponse {

    private int id;

    private String name;

    private Boolean isPrivate;

    private String description;

    private String city;

    private String address;

    private String googleMapsPlaceId;

    private Set<SpecialisationDto> specialisations;

    private Set<WorkingDayDto> workingDays;

    private Set<ContactDto> contacts;

    private Set<ClinicAmenityDto> amenities;

    public ClinicInfoResponse() {}

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

    public Set<ClinicAmenityDto> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<ClinicAmenityDto> amenities) {
        this.amenities = amenities;
    }
}
