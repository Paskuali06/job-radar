package com.opc.jobradar.domain.port.out;

/**
 * Provides password hashing and verification operations.
 */
public interface PasswordHasher {

    String hash(String password);

    boolean matches(String password, String hashedPassword);
}