package com.gmail.romkatsis.healthhubserver.dtos.responses;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.SpecialisationDto;
import com.gmail.romkatsis.healthhubserver.enums.DoctorQualificationCategory;

import java.util.Set;

public class DoctorInfoShortResponse {

    private int id;

    private String firstName;

    private String lastName;

    private DoctorQualificationCategory qualificationCategory;

    private Set<SpecialisationDto> specialisations;

    public DoctorInfoShortResponse() {
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

    public DoctorQualificationCategory getQualificationCategory() {
        return qualificationCategory;
    }

    public void setQualificationCategory(DoctorQualificationCategory qualificationCategory) {
        this.qualificationCategory = qualificationCategory;
    }

    public Set<SpecialisationDto> getSpecialisations() {
        return specialisations;
    }

    public void setSpecialisations(Set<SpecialisationDto> specialisations) {
        this.specialisations = specialisations;
    }
}
