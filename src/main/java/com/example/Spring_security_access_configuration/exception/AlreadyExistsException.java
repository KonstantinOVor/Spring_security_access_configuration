package com.example.Spring_security_access_configuration.exception;

public class AlreadyExistsException extends RuntimeException{

    public AlreadyExistsException(String message) {
        super(message);
    }
}
