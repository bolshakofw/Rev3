package com.example.Demo.DTO;


import lombok.Getter;

@Getter
public class ErrorDetails {
    private long timestamp;
    private String message;

    public ErrorDetails(String message) {
        timestamp = System.currentTimeMillis();
        this.message = message;
    }
}
