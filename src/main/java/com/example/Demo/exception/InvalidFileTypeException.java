package com.example.Demo.exception;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String message){
        super(message);
    }
}