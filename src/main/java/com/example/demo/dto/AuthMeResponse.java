package com.example.demo.dto;

public class AuthMeResponse {
    
    private final String message;
    private final String userId;
    private final String userName;
    private final String userEmail;


    public AuthMeResponse(
        String message,
        String userId,
        String userName,
        String userEmail
    ) {
        this.message = message;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }


    //getters
    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }
}