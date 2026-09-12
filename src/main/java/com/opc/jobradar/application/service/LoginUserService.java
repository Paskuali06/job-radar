package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.port.out.PasswordHasher;
import com.opc.jobradar.domain.port.out.UserRepository;

/**
 * Authenticates Job-Radar users.
 */
public class LoginUserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public LoginUserService(
            UserRepository userRepository,
            PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return false;
        }

        return passwordHasher.matches(
                password,
                user.getPassword()
        );
    }
}