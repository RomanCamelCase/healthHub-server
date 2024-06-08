package com.gmail.romkatsis.healthhubserver.dtos.embedded;

import com.gmail.romkatsis.healthhubserver.enums.ContactType;


public class ContactDto {

    private Integer id;

    private ContactType contactType;

    private String value;

    public ContactDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
