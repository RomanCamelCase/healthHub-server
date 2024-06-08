package com.gmail.romkatsis.healthhubserver.dtos.responses;

public class SecretCodeResponse {

    private String secretCode;

    public SecretCodeResponse(String secretCode) {
        this.secretCode = secretCode;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }
}
