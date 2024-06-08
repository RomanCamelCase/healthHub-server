package com.gmail.romkatsis.healthhubserver.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Review {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "posted_at")
    @CreationTimestamp
    private LocalDateTime postedAt;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Review() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
