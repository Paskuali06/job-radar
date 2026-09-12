package com.opc.jobradar.infrastructure.security;

import com.opc.jobradar.domain.port.out.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Hashes and verifies passwords using BCrypt.
 */
public class BCryptPasswordHasher implements PasswordHasher {

    private final BCryptPasswordEncoder passwordEncoder;

    public BCryptPasswordHasher() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}