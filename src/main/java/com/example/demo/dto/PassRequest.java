package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class PassRequest {
    @NotBlank(message = "Please chose a title for your new password")
    private String title;
    // private String secret;

    //constructor
    public PassRequest() {

    }



    //getters - setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // public String getSecret() {
    //     return secret;
    // }

    // public void setSecret(String secret) {
    //     this.secret = secret;
    // }


}