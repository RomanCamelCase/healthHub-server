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

    public @NotBlank @Size(max = 128) @Email(regexp = "^[A-Za-z0-9]+(\\.[A-Za-z0-9])*@[A-Za-z0-9]{3,}\\.[A-Za-z]{2,}$") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Size(max = 128) @Email(regexp = "^[A-Za-z0-9]+(\\.[A-Za-z0-9])*@[A-Za-z0-9]{3,}\\.[A-Za-z]{2,}$") String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 8, max = 128) @Pattern(regexp = "^[A-Za-z0-9]*$") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, max = 128) @Pattern(regexp = "^[A-Za-z0-9]*$") String password) {
        this.password = password;
    }

    public @NotBlank @Size(min = 2, max = 64) String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank @Size(min = 2, max = 64) String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank @Size(min = 2, max = 64) String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank @Size(min = 2, max = 64) String lastName) {
        this.lastName = lastName;
    }

    public @NotNull Gender getGender() {
        return gender;
    }

    public void setGender(@NotNull Gender gender) {
        this.gender = gender;
    }

    public @NotNull @Past LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotNull @Past LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
