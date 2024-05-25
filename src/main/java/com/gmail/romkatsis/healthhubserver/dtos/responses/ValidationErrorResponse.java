package com.gmail.romkatsis.healthhubserver.dtos.responses;

import java.util.List;
import java.util.Map;

public class ValidationErrorResponse extends PlainErrorResponse {

    private Map<String, List<String>> validationErrors;

    public ValidationErrorResponse(int statusCode,
                                   String path,
                                   String message, Map<String, List<String>> validationErrors) {
        super(statusCode, path, message);
        this.validationErrors = validationErrors;
    }

    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, List<String>> validationErrors) {
        this.validationErrors = validationErrors;
    }
}

