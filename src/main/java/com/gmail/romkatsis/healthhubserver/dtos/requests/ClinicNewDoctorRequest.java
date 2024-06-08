package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ClinicNewDoctorRequest {

    @NotNull
    private Integer doctorId;

    @NotBlank
    private String secretCode;

    public ClinicNewDoctorRequest() {
    }

    public @NotNull Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(@NotNull Integer doctorId) {
        this.doctorId = doctorId;
    }

    public @NotBlank String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(@NotBlank String secretCode) {
        this.secretCode = secretCode;
    }
}
