package com.gmail.romkatsis.healthhubserver.dtos.requests;

import com.gmail.romkatsis.healthhubserver.enums.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class RegistrationRequest {
    @NotBlank
    @Size(max = 128)
    @Email(regexp = "^[A-Za-z0-9]+(\\.[A-Za-z0-9])*@[A-Za-z0-9]{3,}\\.[A-Za-z]{2,}$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 128)
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String password;

    @NotBlank
    @Size(min = 2, max = 64)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 64)
    private String lastName;

    @NotNull
    private Gender gender;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
