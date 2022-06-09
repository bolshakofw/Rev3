package com.example.Demo.dto;


import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ExceptionDto {
    private final Timestamp timestamp;
    private final String message;

    public ExceptionDto(String message) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.message = message;
    }
}
