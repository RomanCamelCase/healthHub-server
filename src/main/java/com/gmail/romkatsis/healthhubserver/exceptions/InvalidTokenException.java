package com.gmail.romkatsis.healthhubserver.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException(String msg) {
        super(msg);
    }

    public InvalidTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
