package com.gmail.romkatsis.healthhubserver.dtos.requests;

import com.gmail.romkatsis.healthhubserver.enums.ContactType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContactRequest {

    @NotNull
    private ContactType contactType;

    @NotNull
    @Size(min = 6, max = 128)
    private String value;

    public ContactRequest() {
    }

    public @NotNull ContactType getContactType() {
        return contactType;
    }

    public void setContactType(@NotNull ContactType contactType) {
        this.contactType = contactType;
    }

    public @NotNull @Size(min = 6, max = 128) String getValue() {
        return value;
    }

    public void setValue(@NotNull @Size(min = 6, max = 128) String value) {
        this.value = value;
    }
}
