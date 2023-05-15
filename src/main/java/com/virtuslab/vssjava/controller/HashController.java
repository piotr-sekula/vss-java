package com.virtuslab.vssjava.controller;

import com.virtuslab.vssjava.domain.Password;
import com.virtuslab.vssjava.service.HashService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class HashController {

    private final HashService hashService;

    public HashController(HashService hashService) {
        this.hashService = hashService;
    }

    @GetMapping("/hash")
    String dummyGetEndpoint() {
        return "Hello Hash!";
    }

    @PostMapping(value = "/hash")
    PasswordWithHashView calculateHash(@RequestBody HashRequest request) {
        try {
            Password password = hashService.calculatePasswordHash(request);
            return PasswordWithHashView.fromPassword(password);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown hashing algorithm: " + request.hashType());
        }
    }
}
