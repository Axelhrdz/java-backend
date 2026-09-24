package com.example.demo.exception;

public class PassTitleAlreadyExistsException extends RuntimeException {
    public PassTitleAlreadyExistsException(String message) {
        super(message);
    }
}