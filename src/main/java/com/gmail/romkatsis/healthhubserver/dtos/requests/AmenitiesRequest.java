package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class AmenitiesRequest {

    @NotNull
    private Set<Integer> amenities;

    public AmenitiesRequest() {
    }

    public @NotNull Set<Integer> getAmenities() {
        return amenities;
    }

    public void setAmenities(@NotNull Set<Integer> amenities) {
        this.amenities = amenities;
    }
}
