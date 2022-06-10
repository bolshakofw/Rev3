package com.example.Demo.errors.exception.users;

public class ChangePasswordException extends RuntimeException {
    public ChangePasswordException(String message) {
        super(message);
    }
}
