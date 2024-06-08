package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClinicInfoRequest {

    @NotBlank
    @Size(min = 2, max = 128)
    private String name;

    @NotNull
    private Boolean isPrivate;

    private String description;

    @NotNull
    @Size(min = 2, max = 64)
    private String city;

    @NotNull
    @Size(min = 2, max = 128)
    private String address;

    public ClinicInfoRequest() {
    }

    public @NotBlank @Size(min = 2, max = 128) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(min = 2, max = 128) String name) {
        this.name = name;
    }

    public @NotNull Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(@NotNull Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull @Size(min = 2, max = 64) String getCity() {
        return city;
    }

    public void setCity(@NotNull @Size(min = 2, max = 64) String city) {
        this.city = city;
    }

    public @NotNull @Size(min = 2, max = 128) String getAddress() {
        return address;
    }

    public void setAddress(@NotNull @Size(min = 2, max = 128) String address) {
        this.address = address;
    }
}
