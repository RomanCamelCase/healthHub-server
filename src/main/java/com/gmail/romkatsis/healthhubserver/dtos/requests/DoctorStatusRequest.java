package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotNull;

public class DoctorStatusRequest {

    @NotNull
    private boolean active;

    public DoctorStatusRequest() {
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
