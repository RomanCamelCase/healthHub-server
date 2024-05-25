package com.gmail.romkatsis.healthhubserver.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
