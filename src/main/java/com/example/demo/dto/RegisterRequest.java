package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    private String name;

    @NotBlank(message = "Email address is required")
    @Email(
        message = "Please provide a valid email address",
        regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"
    )
    private String email;
    private String password;

    //constructor
    public RegisterRequest() {

    }


    //getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
