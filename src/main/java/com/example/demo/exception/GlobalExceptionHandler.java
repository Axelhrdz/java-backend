package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<?> handleDuplicate(EmailAlreadyRegisteredException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "error", e.getMessage()
        ));
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
            "error", e.getMessage()
        ));
    }


    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handleValidPassword(InvalidPasswordException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
            "error", e.getMessage()
        ));
    }



}
