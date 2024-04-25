package com.gmail.romkatsis.healthhubserver.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_CUSTOMER, ROLE_DOCTOR, ROLE_CLINIC_ADMINISTRATOR, ROLE_MANAGER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
