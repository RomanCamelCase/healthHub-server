package com.gmail.romkatsis.healthhubserver.dtos.requests;

import com.gmail.romkatsis.healthhubserver.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserInfoRequest {

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
    private LocalDate birthDate;

    public UserInfoRequest() {}

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

    public @NotNull @Past LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NotNull @Past LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
