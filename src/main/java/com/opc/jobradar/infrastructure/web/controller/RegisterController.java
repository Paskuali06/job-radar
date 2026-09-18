package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.RegisterUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class RegisterController {

    private final RegisterUserService registerUserService;

    public RegisterController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterUserRequest request) {
        registerUserService.register(
                request.name(),
                request.email(),
                request.password()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException() {
        return ResponseEntity.badRequest().build();
    }
}