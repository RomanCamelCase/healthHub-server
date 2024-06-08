package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class SpecialisationsRequest {

    @NotEmpty
    private Set<Integer> specialisations;

    public SpecialisationsRequest() {
    }

    public @NotEmpty Set<Integer> getSpecialisations() {
        return specialisations;
    }

    public void setSpecialisations(@NotEmpty Set<Integer> specialisations) {
        this.specialisations = specialisations;
    }
}
