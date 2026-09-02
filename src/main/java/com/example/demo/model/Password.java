package com.example.demo.model;

import java.time.Instant;

public class Password {
    private final String id;
    // private final String userId;
    private final String title;
    private final String secret;
    private final Instant createdAt;

    public Password(String id, String title, String secret, Instant createdAt) {
        this.id = id;
        // this.userId = userId;
        this.title = title;
        this.secret = secret;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    // public String getUserId() {
    //     return userId;
    // }

    public String getTitle() {
        return title;
    }

    public String getSecret() {
        return secret;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
