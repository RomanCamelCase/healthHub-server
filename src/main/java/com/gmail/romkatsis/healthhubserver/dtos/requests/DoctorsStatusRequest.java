package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotNull;

public class DoctorsStatusRequest {

    @NotNull
    private boolean isActive;

    public DoctorsStatusRequest() {
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
