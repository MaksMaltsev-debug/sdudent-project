package com.maltsev.jwt.exception;

public class AccountNotSetException extends RuntimeException {
    public AccountNotSetException(String message) {
        super(message);
    }
}
