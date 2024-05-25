package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank
    @Size(max = 128)
    @Email(regexp = "^[A-Za-z0-9]+(\\.[A-Za-z0-9])*@[A-Za-z0-9]{3,}\\.[A-Za-z]{2,}$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 128)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
