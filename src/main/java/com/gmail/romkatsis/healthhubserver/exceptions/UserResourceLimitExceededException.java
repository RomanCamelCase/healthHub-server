package com.gmail.romkatsis.healthhubserver.exceptions;

public class UserResourceLimitExceededException extends RuntimeException {

    public UserResourceLimitExceededException(String message) {
        super(message);
    }
}
