package com.example.Demo.exception;

public class FileDataNotFoundException extends RuntimeException {
    public FileDataNotFoundException(String message) {
        super(message);
    }
}
