package com.opc.jobradar.application.exception;

/**
 * Thrown when user authentication credentials are invalid.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}