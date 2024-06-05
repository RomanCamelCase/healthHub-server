package com.gmail.romkatsis.healthhubserver.dtos.responses;

import com.gmail.romkatsis.healthhubserver.enums.ContactType;

public class ContactResponse {

    private ContactType contactType;

    private String value;

    public ContactResponse() {
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
