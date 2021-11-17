package com.ua.semkov.smartsport.exceptions;

public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException() {
    }
    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
