package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.domain.port.out.PasswordHasher;
import com.opc.jobradar.domain.port.out.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Registers new Job-Radar users.
 */

@Service
public class RegisterUserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserService(
            UserRepository userRepository,
            PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public void register(String name, String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordHasher.hash(password));
        user.setRole(UserRole.USER);

        userRepository.save(user);
    }
}