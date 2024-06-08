package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotNull;

public class DoctorStatusRequest {

    @NotNull
    private boolean isActive;

    public DoctorStatusRequest() {
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
