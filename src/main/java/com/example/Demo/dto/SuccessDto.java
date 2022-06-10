package com.example.Demo.dto;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class SuccessDto {
    private final Timestamp timestamp;
    private final String message;

    public SuccessDto(String message) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.message = message;
    }
}
