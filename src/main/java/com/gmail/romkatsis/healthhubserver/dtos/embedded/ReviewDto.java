package com.gmail.romkatsis.healthhubserver.dtos.embedded;

import java.time.LocalDateTime;

public class ReviewDto {

    private Integer id;

    private LocalDateTime postedAt;

    private Boolean isAnonymous;

    private Integer rating;

    private String text;

    public ReviewDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public Boolean getAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
