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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

//jwt http only cookier imports
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import java.time.Duration;

//auth me response
import com.example.demo.dto.AuthMeResponse;
import org.springframework.web.bind.annotation.RequestAttribute;

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


    //auth/me -- check if user authenticated -- jwt
    @GetMapping("/me")
    public ResponseEntity<AuthMeResponse> authMe(@RequestAttribute("userId") String userId) {

        User user = authService.authMe(userId);

        AuthMeResponse response = new AuthMeResponse(
            "User Authenticated",
            user.getId(),
            user.getName(),
            user.getEmail()
        );

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);


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
