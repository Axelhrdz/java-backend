package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//annotation custom exceptions imports
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

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


    //checks @Valid @RequestBody annotation, in case of null or wrong body parameters 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("Invalid request");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "error", message
        ));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleUnreadableBodyReq(HttpMessageNotReadableException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "error", "A valid JSON request body is required"
        ));
    }



    //duplicate passwors title
    @ExceptionHandler(PassTitleAlreadyExistsException.class)
    public ResponseEntity<?> handleDuplicatePassTitle(PassTitleAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "error", e.getMessage()
        ));
    }



}
