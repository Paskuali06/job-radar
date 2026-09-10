package com.opc.jobradar.application.exception;

/**
 * Excepción lanzada cuando una oferta de empleo ya existe.
 */
public class JobOfferAlreadyExistsException extends RuntimeException {

    public JobOfferAlreadyExistsException() {
        super("Esta oferta ya existe");
    }
}