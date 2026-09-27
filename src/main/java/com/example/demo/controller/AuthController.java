package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

//jwt http only cookier imports
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import java.time.Duration;

import java.util.Map;





@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);
        String token = jwtService.createToken(user);

        ResponseCookie cookie = ResponseCookie
            .from("access_token", token)
            .httpOnly(true)
            .secure(false) //on production, set to true, for HTTPS
            .sameSite("Lax")
            .path("/")
            .maxAge(Duration.ofMillis(jwtService.getExpirationMs()))
            .build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(Map.of(
            "message", "User registered",
            "id", user.getId(),
            "email", user.getEmail(),
            "token", token
        ));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    
        User user = authService.login(request);
        String token = jwtService.createToken(user);

        ResponseCookie cookie = ResponseCookie
            .from("access_token", token)
            .httpOnly(true)
            .secure(false) //on production, set to true, for HTTPS
            .sameSite("Lax")
            .path ("/")
            .maxAge(Duration.ofMillis(jwtService.getExpirationMs()))
            .build();



        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(Map.of(
            "message", "Login sucessfully",
            "email", user.getEmail(),
            "token", token
        ));

    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("Invalid request");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "error", message
        ));
    }
}
