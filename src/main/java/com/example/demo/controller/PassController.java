package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Password;
import com.example.demo.service.PassService;
import com.example.demo.dto.PassRequest;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/pass")
public class PassController {

    private final PassService passService;
    

    public PassController(PassService passService) {
        this.passService = passService;
    }


    //endpoints
    @PostMapping("/generate")
    public ResponseEntity<?> generatePassword(@Valid @RequestBody PassRequest request, @RequestAttribute("userId") String userId) {

        Password password = passService.passGenerator(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body (Map.of(
            "message", "Generate password endpoint",
            "title", request.getTitle(),
            "password", password.getSecret()
        ));
    }
}
