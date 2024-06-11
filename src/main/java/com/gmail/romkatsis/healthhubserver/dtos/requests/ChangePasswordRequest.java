package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank
    @Size(min = 8, max = 128)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String newPassword;

    @NotBlank
    private String token;

    public ChangePasswordRequest() {
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
