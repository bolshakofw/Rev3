package com.example.Demo.DTO;


import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
public class ErrorDetails {
    private Timestamp timestamp;
    private String message;

    public ErrorDetails(String message) {
        timestamp = new Timestamp(System.currentTimeMillis());
        this.message = message;
    }
}
