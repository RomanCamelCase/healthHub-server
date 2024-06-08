package com.gmail.romkatsis.healthhubserver.dtos.requests;

import jakarta.validation.constraints.*;

public class ReviewRequest {

    @NotNull
    private Boolean isAnonymous;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    private String text;

    public ReviewRequest() {
    }

    public @NotNull Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(@NotNull Boolean anonymous) {
        isAnonymous = anonymous;
    }

    public @NotNull @Min(1) @Max(5) Integer getRating() {
        return rating;
    }

    public void setRating(@NotNull @Min(1) @Max(5) Integer rating) {
        this.rating = rating;
    }

    public @NotBlank String getText() {
        return text;
    }

    public void setText(@NotBlank String text) {
        this.text = text;
    }
}
