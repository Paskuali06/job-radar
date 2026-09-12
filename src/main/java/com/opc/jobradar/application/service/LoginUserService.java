package com.opc.jobradar.application.service;

import com.opc.jobradar.application.exception.InvalidCredentialsException;
import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.port.out.PasswordHasher;
import com.opc.jobradar.domain.port.out.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Authenticates Job-Radar users.
 */
@Service
public class LoginUserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public LoginUserService(
            UserRepository userRepository,
            PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null ||
                !passwordHasher.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return user;
    }
}