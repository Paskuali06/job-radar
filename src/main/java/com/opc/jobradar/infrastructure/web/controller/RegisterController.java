package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.RegisterUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController 
@RequestMapping ("/auth/")
public class RegisterController {
    
    private final RegisterUserService registerUserService;

    public RegisterController(RegisterUserService registerUserService){
        this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    @ResponseStatus (HttpStatus.CREATED)
    public void register(@RequestBody Map<String, String> request){
        registerUserService.register(
                request.get("name"),
                request.get("email"),
                request.get("password")
        );
    }
}